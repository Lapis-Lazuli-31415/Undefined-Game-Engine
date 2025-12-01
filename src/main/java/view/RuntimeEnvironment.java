package view;

import entity.Scene;
import entity.GameObject;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.event.Event;
import entity.scripting.event.OnKeyPressEvent;
import entity.scripting.event.OnClickEvent;
import entity.scripting.environment.Environment;
import entity.event_listener.EventListener;
import interface_adapter.preview.EventListenerFactory;
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
 * @author Wanru Cheng
 */
public class RuntimeEnvironment {

    private final Scene scene;
    private final GameCanvas canvas;
    private final Environment globalEnvironment;
    private InputManager inputManager;
    private final EventListenerFactory listenerFactory;

    private final TriggerExecutionInputBoundary triggerExecutor;

    private final Map<Trigger, EventListener> listeners;
    private final Map<Trigger, GameObject> triggerOwners;
    private Timer gameLoopTimer;
    private boolean running;

    private long lastFpsTime;
    private int frameCount;
    private int currentFps;

    public RuntimeEnvironment(Scene scene, GameCanvas canvas,
                              Environment globalEnvironment,
                              EventListenerFactory listenerFactory) {
        this.scene = scene;
        this.canvas = canvas;
        this.globalEnvironment = globalEnvironment;

        if (listenerFactory != null) {
            this.listenerFactory = listenerFactory;
            this.inputManager = (InputManager) listenerFactory.getInputState();  // ✅ Cast from interface
        } else {
            this.inputManager = new InputManager();
            this.listenerFactory = new EventListenerFactory(this.inputManager);
        }

        this.listeners = new HashMap<>();
        this.triggerOwners = new HashMap<>();
        this.running = false;

        TriggerExecutionPresenter presenter = new TriggerExecutionPresenter();
        this.triggerExecutor = new TriggerExecutionInteractor(presenter);

        canvas.setGlobalEnvironment(globalEnvironment);

        System.out.println("RuntimeEnvironment created");
    }

    public RuntimeEnvironment(Scene scene, GameCanvas canvas, Environment globalEnvironment) {
        this(scene, canvas, globalEnvironment, null);
    }

    public void start() {
        if (running) {
            System.out.println("RuntimeEnvironment already running");
            return;
        }

        System.out.println("\n=== Starting RuntimeEnvironment ===");

        initializeListeners();

        canvas.setInputManager(inputManager);
        canvas.addKeyListener(inputManager);
        canvas.addMouseListener(inputManager.getMouseListener());
        canvas.addMouseMotionListener(inputManager.getMouseListener());
        canvas.setFocusable(true);
        canvas.requestFocus();

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

    private void initializeListeners() {
        System.out.println("Initializing event listeners...");
        System.out.println("Mode: " + (canvas.isUsingButtonMode() ? "Button" : "Collision"));

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

    private void gameLoop() {
        for (Map.Entry<Trigger, EventListener> entry : listeners.entrySet()) {
            Trigger trigger = entry.getKey();
            EventListener listener = entry.getValue();

            if (listener.isTriggered()) {
                GameObject obj = triggerOwners.get(trigger);
                TriggerExecutionInputData inputData = new TriggerExecutionInputData(
                        trigger,
                        obj,
                        globalEnvironment,
                        scene
                );
                triggerExecutor.execute(inputData);
                canvas.requestFocusInWindow();
            }
        }

        canvas.resetClickListeners();
        inputManager.update();
        canvas.repaint();
        updateFps();
    }

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

    public boolean isRunning() {
        return running;
    }

    public int getFps() {
        return currentFps;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public EventListenerFactory getListenerFactory() {
        return listenerFactory;
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }
}