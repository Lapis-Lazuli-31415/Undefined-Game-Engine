package view;

import entity.Scene;
import entity.GameObject;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.event.Event;
import entity.scripting.event.OnKeyPressEvent;
import entity.scripting.event.OnClickEvent;
import entity.scripting.condition.Condition;
import entity.scripting.action.Action;
import entity.scripting.environment.Environment;
import entity.Eventlistener.EventListener;
import entity.InputManager;
import interface_adapter.preview.EventListenerFactory;

import javax.swing.Timer;
import java.util.HashMap;
import java.util.Map;

/**
 * RuntimeEnvironment - Game loop engine for preview/testing
 * Part of View layer (blue ring in CA diagram).
 *
 * Responsibilities:
 * - Run 60 FPS game loop
 * - Check trigger conditions
 * - Execute trigger actions
 * - Manage input state
 * - Coordinate with EventListenerFactory for listener creation
 *
 * OPTIMIZED: Now uses Factory pattern to create listeners
 * instead of direct instantiation.
 *
 * @author Wanru Cheng
 */
public class RuntimeEnvironment {

    private final Scene scene;
    private final GameCanvas canvas;
    private final Environment globalEnvironment;
    private final InputManager inputManager;
    private final EventListenerFactory listenerFactory;

    private final Map<Trigger, EventListener> listeners;
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
//    public RuntimeEnvironment(Scene scene, GameCanvas canvas,
//                              Environment globalEnvironment,
//                              EventListenerFactory listenerFactory) {
//        this.scene = scene;
//        this.canvas = canvas;
//        this.globalEnvironment = globalEnvironment;
//        this.inputManager = listenerFactory != null
//                ? new InputManager()
//                : new InputManager(); // Create InputManager here
//        this.listenerFactory = listenerFactory != null
//                ? listenerFactory
//                : new EventListenerFactory(this.inputManager); // Create factory if not provided
//        this.listeners = new HashMap<>();
//        this.running = false;
//
//        // Pass global environment to canvas
//        canvas.setGlobalEnvironment(globalEnvironment);
//
//        System.out.println("RuntimeEnvironment created");
//    }
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
        this.running = false;

        // Pass global environment to canvas
        canvas.setGlobalEnvironment(globalEnvironment);

        System.out.println("RuntimeEnvironment created");
    }

    /**
     * Convenience constructor that creates its own InputManager and Factory.
     * Used for simpler initialization from PreviewWindow.
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
     *
     * Workflow:
     * 1. Initialize all event listeners using Factory
     * 2. Attach InputManager to canvas
     * 3. Start 60 FPS game loop
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
     * OPTIMIZED: Uses EventListenerFactory instead of direct instantiation.
     *
     * Scans all GameObjects and creates appropriate listeners
     * for each trigger's event type using the Factory.
     */
    private void initializeListeners() {
        System.out.println("Initializing event listeners...");

        int keyListenerCount = 0;
        int clickListenerCount = 0;

        for (GameObject obj : scene.getGameObjects()) {
            if (!obj.isActive()) continue;

            TriggerManager tm = obj.getTriggerManager();
            if (tm == null) continue;

            for (Trigger trigger : tm.getAllTriggers()) {
                Event event = trigger.getEvent();
                EventListener listener = null;

                // Use Factory to create appropriate listener
                if (event instanceof OnKeyPressEvent) {
                    OnKeyPressEvent keyEvent = (OnKeyPressEvent) event;
                    listener = listenerFactory.createKeyPressListener(keyEvent);
                    keyListenerCount++;
                    System.out.println("  ✓ Created KeyPressListener for key: " + keyEvent.getKey());

                } else if (event instanceof OnClickEvent) {
                    listener = listenerFactory.createCollisionClickListener(obj);
                    clickListenerCount++;
                    System.out.println("  ✓ Created ClickListener (collision mode) for: " + obj.getName());
                }

                // Store listener
                if (listener != null) {
                    listeners.put(trigger, listener);
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
     *
     * Workflow:
     * 1. Check all event listeners
     * 2. Execute triggered actions
     * 3. Reset click listeners
     * 4. Update input manager
     * 5. Repaint canvas
     * 6. Update FPS
     */
    private void gameLoop() {
        // 1. Check all triggers
        for (Map.Entry<Trigger, EventListener> entry : listeners.entrySet()) {
            Trigger trigger = entry.getKey();
            EventListener listener = entry.getValue();

            // Check if event is triggered
            if (listener.isTriggered()) {
                executeTrigger(trigger, getGameObjectForTrigger(trigger));
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
     * Execute a trigger.
     *
     * @param trigger The trigger to execute
     * @param obj The GameObject that owns this trigger
     */
    private void executeTrigger(Trigger trigger, GameObject obj) {
        try {
            Environment localEnv = obj != null ? obj.getEnvironment() : new Environment();

            // Check all conditions
            boolean allConditionsMet = true;
            for (Condition condition : trigger.getConditions()) {
                if (!condition.evaluate(globalEnvironment, localEnv)) {
                    allConditionsMet = false;
                    break;
                }
            }

            // Execute actions if conditions met
            if (allConditionsMet) {
                for (Action action : trigger.getActions()) {
                    action.execute(globalEnvironment, localEnv);
                }

                // Log execution
                String objName = obj != null ? obj.getName() : "Unknown";
                String eventName = trigger.getEvent().toString();
                System.out.println("✓ Executed: " + eventName + " on " + objName);
            }

        } catch (Exception e) {
            System.err.println("Error executing trigger: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Find the GameObject that owns a trigger.
     */
    private GameObject getGameObjectForTrigger(Trigger trigger) {
        for (GameObject obj : scene.getGameObjects()) {
            TriggerManager tm = obj.getTriggerManager();
            if (tm != null && tm.getAllTriggers().contains(trigger)) {
                return obj;
            }
        }
        return null;
    }

    /**
     * Update FPS counter.
     */
    private void updateFps() {
        frameCount++;

        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - lastFpsTime;

        // Update FPS every second
        if (elapsed >= 1000) {
            currentFps = frameCount;
            frameCount = 0;
            lastFpsTime = currentTime;

            // Update canvas FPS display
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

        // Stop game loop
        if (gameLoopTimer != null) {
            gameLoopTimer.stop();
        }

        // Remove input manager from canvas (keyboard + mouse)
        canvas.removeKeyListener(inputManager);
        canvas.removeMouseListener(inputManager.getMouseListener());
        canvas.removeMouseMotionListener(inputManager.getMouseListener());

        // Reset input state
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