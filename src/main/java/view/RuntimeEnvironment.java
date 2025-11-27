package view;

import entity.Scene;
import entity.GameObject;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.event.Event;
import entity.scripting.event.OnKeyPressEvent;
import entity.scripting.event.OnClickEvent;
import entity.scripting.environment.Environment;
import entity.Eventlistener.EventListener;
import entity.InputManager;
import interface_adapter.runtime.EventListenerFactory;
import interface_adapter.runtime.TriggerExecutionPresenter;
import use_case.runtime.TriggerExecutionInputBoundary;
import use_case.runtime.TriggerExecutionInputData;
import use_case.runtime.TriggerExecutionInteractor;

import javax.swing.Timer;
import java.util.HashMap;
import java.util.Map;

/**
 * RuntimeEnvironment - Game loop engine for preview/testing
 * Part of View layer (blue ring in CA diagram).
 *
 * Responsibilities:
 * - Run 60 FPS game loop
 * - Detect events via listeners
 * - Delegate trigger execution to Use Case layer
 * - Manage input state
 *
 * @author Wanru Cheng
 */
public class RuntimeEnvironment {

    private final Scene scene;
    private final GameCanvas canvas;
    private final Environment globalEnvironment;
    private final InputManager inputManager;
    private final EventListenerFactory listenerFactory;

    // Use Case for trigger execution
    private final TriggerExecutionInputBoundary triggerExecutor;

    private final Map<Trigger, EventListener> listeners;
    private final Map<Trigger, GameObject> triggerOwners;
    private Timer gameLoopTimer;
    private boolean running;

    private long lastFpsTime;
    private int frameCount;
    private int currentFps;

    /**
     * Create a RuntimeEnvironment.
     *
     * @param scene The scene to run
     * @param canvas The canvas to render on
     * @param globalEnvironment The global environment for triggers
     * @param listenerFactory Factory for creating event listeners
     */
    public RuntimeEnvironment(Scene scene, GameCanvas canvas,
                              Environment globalEnvironment,
                              EventListenerFactory listenerFactory) {
        this.scene = scene;
        this.canvas = canvas;
        this.globalEnvironment = globalEnvironment;

        if (listenerFactory != null) {
            this.listenerFactory = listenerFactory;
            this.inputManager = listenerFactory.getInputManager();
        } else {
            this.inputManager = new InputManager();
            this.listenerFactory = new EventListenerFactory(this.inputManager);
        }

        this.listeners = new HashMap<>();
        this.triggerOwners = new HashMap<>();
        this.running = false;

        // Create Use Case components for trigger execution
        TriggerExecutionPresenter presenter = new TriggerExecutionPresenter();
        this.triggerExecutor = new TriggerExecutionInteractor(presenter);

        // Pass global environment to canvas
        canvas.setGlobalEnvironment(globalEnvironment);

        System.out.println("RuntimeEnvironment created");
    }

    /**
     * Convenience constructor that creates its own InputManager and Factory.
     *
     * @param scene The scene to run
     * @param canvas The canvas to render on
     * @param globalEnvironment The global environment for triggers
     */
    public RuntimeEnvironment(Scene scene, GameCanvas canvas, Environment globalEnvironment) {
        this(scene, canvas, globalEnvironment, null);
    }

    /**
     * Start the runtime.
     */
    public void start() {
        if (running) {
            System.out.println("RuntimeEnvironment already running");
            return;
        }

        System.out.println("\n=== Starting RuntimeEnvironment ===");

        // 1. Initialize listeners using Factory
        initializeListeners();

        // 2. Attach InputManager to canvas (keyboard + mouse)
        canvas.addKeyListener(inputManager);
        canvas.addMouseListener(inputManager.getMouseListener());
        canvas.addMouseMotionListener(inputManager.getMouseListener());
        canvas.setFocusable(true);
        canvas.requestFocus();

        // 3. Start game loop (60 FPS = ~16ms per frame)
        gameLoopTimer = new Timer(16, e -> gameLoop());
        gameLoopTimer.start();

        running = true;
        lastFpsTime = System.currentTimeMillis();
        frameCount = 0;

        System.out.println("=== RuntimeEnvironment Started ===\n");
        System.out.println("Total Listeners: " + listeners.size());
        System.out.println("Game loop running at 60 FPS");
        System.out.println("\nPress keys or click objects to test triggers!\n");
    }

    /**
     * Initialize event listeners for all triggers.
     */
    private void initializeListeners() {
        System.out.println("Initializing event listeners...");
        System.out.println("Mode: " + (canvas.isUsingButtonMode() ? "Button" : "Collision"));

        // Clear previous listeners
        listeners.clear();
        triggerOwners.clear();

        int keyListenerCount = 0;
        int clickListenerCount = 0;

        for (GameObject obj : scene.getGameObjects()) {
            if (!obj.isActive()) continue;

            TriggerManager tm = obj.getTriggerManager();
            if (tm == null) continue;

            for (Trigger trigger : tm.getAllTriggers()) {
                Event event = trigger.getEvent();
                EventListener listener = null;

                if (event instanceof OnKeyPressEvent) {
                    OnKeyPressEvent keyEvent = (OnKeyPressEvent) event;
                    listener = listenerFactory.createKeyPressListener(keyEvent);
                    keyListenerCount++;
                    System.out.println("  + Created KeyPressListener for key: " + keyEvent.getKey());

                } else if (event instanceof OnClickEvent) {
                    if (canvas.isUsingButtonMode()) {
                        listener = canvas.getClickListener(obj);
                        if (listener != null) {
                            clickListenerCount++;
                            System.out.println("  + Retrieved ClickListener (button mode) for: " + obj.getName());
                        } else {
                            System.err.println("  - Failed to get ClickListener for: " + obj.getName());
                        }
                    } else {
                        listener = listenerFactory.createCollisionClickListener(obj);
                        clickListenerCount++;
                        System.out.println("  + Created ClickListener (collision mode) for: " + obj.getName());
                    }
                }

                if (listener != null) {
                    listeners.put(trigger, listener);
                    triggerOwners.put(trigger, obj);
                }
            }
        }

        System.out.println("\nListener Summary:");
        System.out.println("  KeyPressListeners: " + keyListenerCount);
        System.out.println("  ClickListeners: " + clickListenerCount);
        System.out.println("  Total: " + listeners.size());
    }

    /**
     * Main game loop (called every ~16ms for 60 FPS).
     */
    private void gameLoop() {
        // 1. Check all triggers and delegate execution to Use Case
        for (Map.Entry<Trigger, EventListener> entry : listeners.entrySet()) {
            Trigger trigger = entry.getKey();
            EventListener listener = entry.getValue();

            if (listener.isTriggered()) {
                // Delegate to Use Case layer
                GameObject obj = triggerOwners.get(trigger);
                TriggerExecutionInputData inputData = new TriggerExecutionInputData(
                        trigger,
                        obj,
                        globalEnvironment
                );
                triggerExecutor.execute(inputData);
            }
        }

        // 2. Reset click listeners for next frame
        canvas.resetClickListeners();

        // 3. Update input manager (clear just pressed keys)
        inputManager.update();

        // 4. Repaint canvas
        canvas.repaint();

        // 5. Update FPS counter
        updateFps();
    }

    /**
     * Update FPS counter.
     */
    private void updateFps() {
        frameCount++;

        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - lastFpsTime;

        if (elapsed >= 1000) {
            currentFps = frameCount;
            frameCount = 0;
            lastFpsTime = currentTime;
            canvas.setFps(currentFps);
        }
    }

    /**
     * Stop the runtime.
     */
    public void stop() {
        if (!running) {
            System.out.println("RuntimeEnvironment not running");
            return;
        }

        System.out.println("\n=== Stopping RuntimeEnvironment ===");

        if (gameLoopTimer != null) {
            gameLoopTimer.stop();
        }

        canvas.removeKeyListener(inputManager);
        canvas.removeMouseListener(inputManager.getMouseListener());
        canvas.removeMouseMotionListener(inputManager.getMouseListener());

        inputManager.reset();
        running = false;

        System.out.println("RuntimeEnvironment stopped\n");
    }

    /**
     * Check if runtime is running.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Get current FPS.
     */
    public int getFps() {
        return currentFps;
    }

    /**
     * Get the input manager.
     */
    public InputManager getInputManager() {
        return inputManager;
    }

    /**
     * Get the listener factory.
     */
    public EventListenerFactory getListenerFactory() {
        return listenerFactory;
    }
}