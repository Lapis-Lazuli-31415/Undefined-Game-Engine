package view;

import entity.GameObject;
import entity.Scene;
import app.InputManager;

import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.event.Event;
import entity.scripting.event.OnKeyPressEvent;
import entity.scripting.event.OnClickEvent;
import entity.Eventlistener.EventListener;
import entity.Eventlistener.KeyPressListener;
import entity.Eventlistener.ClickListener;
import entity.scripting.condition.Condition;
import entity.scripting.action.Action;
import entity.scripting.environment.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * RuntimeEnvironment - Game loop with unified listener system
 *
 * @author Wanru Cheng
 */
public class RuntimeEnvironment {

    private Scene scene;
    private GameCanvas canvas;
    private InputManager inputManager;
    private Environment globalEnvironment;

    private Map<Trigger, EventListener> listenerMap;

    private boolean running;
    private int targetFPS = 60;
    private int currentFPS = 0;

    private Thread gameThread;

    public RuntimeEnvironment(Scene scene, GameCanvas canvas, Environment globalEnvironment) {
        this.scene = scene;
        this.canvas = canvas;
        this.globalEnvironment = globalEnvironment;
        this.inputManager = new InputManager();
        this.listenerMap = new HashMap<>();
        this.running = false;

        canvas.addKeyListener(inputManager.getKeyListener());
        canvas.addMouseListener(inputManager.getMouseListener());
        canvas.addMouseMotionListener(inputManager.getMouseMotionListener());
        canvas.setFocusable(true);
        canvas.setGlobalEnvironment(globalEnvironment);

        initializeListeners();
    }

    private void initializeListeners() {
        System.out.println("=== Initializing Listeners ===");

        for (GameObject obj : scene.getGameObjects()) {
            TriggerManager tm = obj.getTriggerManager();
            if (tm == null) continue;

            for (Trigger trigger : tm.getAllTriggers()) {
                Event event = trigger.getEvent();
                EventListener listener = null;

                if (event instanceof OnKeyPressEvent) {
                    OnKeyPressEvent keyEvent = (OnKeyPressEvent) event;
                    listener = new KeyPressListener(keyEvent, inputManager);
                    System.out.println("  ✓ KeyPressListener for key: " + keyEvent.getKey());

                } else if (event instanceof OnClickEvent) {
                    listener = canvas.getClickListener(obj);
                    if (listener != null) {
                        System.out.println("  ✓ ClickListener for button: " + obj.getName());
                    }
                }

                if (listener != null) {
                    listenerMap.put(trigger, listener);
                }
            }
        }

        System.out.println("=== Listeners Ready ===\n");
    }

    public void start() {
        if (running) return;
        running = true;
        gameThread = new Thread(this::gameLoop);
        gameThread.start();
        System.out.println("RuntimeEnvironment started");
    }

    public void stop() {
        running = false;
        if (gameThread != null) {
            try {
                gameThread.join(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("RuntimeEnvironment stopped");
    }

    private void gameLoop() {
        long lastTime = System.nanoTime();
        long lastFpsTime = System.nanoTime();
        double nsPerFrame = 1_000_000_000.0 / targetFPS;
        int frameCount = 0;

        while (running) {
            long now = System.nanoTime();

            if ((now - lastTime) >= nsPerFrame) {
                update();
                render();
                lastTime = now;
                frameCount++;
            }

            if (now - lastFpsTime >= 1_000_000_000L) {
                currentFPS = frameCount;
                frameCount = 0;
                lastFpsTime = now;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        checkAllTriggers();
        canvas.resetClickListeners();
        canvas.updateClickablePositions();
        inputManager.update();
    }

    private void render() {
        canvas.setGameObjects(scene.getGameObjects());
        canvas.setFps(currentFPS);
        canvas.repaint();
    }

    private void checkAllTriggers() {
        for (GameObject obj : scene.getGameObjects()) {
            if (!obj.isActive()) continue;

            TriggerManager tm = obj.getTriggerManager();
            if (tm == null) continue;

            for (Trigger trigger : tm.getAllTriggers()) {
                EventListener listener = listenerMap.get(trigger);

                if (listener != null && listener.isTriggered()) {
                    executeTrigger(trigger, obj);
                }
            }
        }
    }

    private void executeTrigger(Trigger trigger, GameObject obj) {
        try {
            Environment localEnv = obj.getEnvironment();

            boolean allConditionsMet = true;
            for (Condition condition : trigger.getConditions()) {
                if (!condition.evaluate(globalEnvironment, localEnv)) {
                    allConditionsMet = false;
                    break;
                }
            }

            if (allConditionsMet) {
                for (Action action : trigger.getActions()) {
                    action.execute(globalEnvironment, localEnv);
                }

                System.out.println("✓ Executed: " + trigger.getEvent() + " on " + obj.getName());
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public int getCurrentFPS() {
        return currentFPS;
    }
}