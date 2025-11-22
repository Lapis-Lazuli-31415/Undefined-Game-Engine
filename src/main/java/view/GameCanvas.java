package view;

import entity.GameObject;
import entity.Transform;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.event.OnClickEvent;
import entity.Eventlistener.ClickListener;
import entity.scripting.condition.Condition;
import entity.scripting.action.Action;
import entity.scripting.environment.Environment;
import interface_adapter.preview.EventListenerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * GameCanvas - Creates UI buttons for GameObjects with OnClickEvent.
 * Part of View layer (blue ring in CA diagram).
 *
 * Responsibilities:
 * - Render game objects
 * - Create UI buttons for clickable objects
 * - Coordinate with EventListenerFactory for click listeners
 * - Display FPS and debug info
 *
 * OPTIMIZED: Now uses Factory pattern to create click listeners
 * instead of direct instantiation.
 *
 * Button label = GameObject.getName()
 *
 * @author Wanru Cheng
 */
public class GameCanvas extends JPanel {

    private ArrayList<GameObject> gameObjects;
    private Map<GameObject, JButton> uiButtons;
    private Map<GameObject, ClickListener> clickListeners;
    private Environment globalEnvironment;
    private EventListenerFactory listenerFactory;

    private int currentFps = 0;
    private boolean showFps = true;

    /**
     * Constructor.
     */
    public GameCanvas() {
        this.gameObjects = new ArrayList<>();
        this.uiButtons = new HashMap<>();
        this.clickListeners = new HashMap<>();

        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(45, 45, 48));
        setLayout(null);
    }

    /**
     * Set the EventListenerFactory.
     * OPTIMIZED: Canvas now receives factory instead of creating listeners directly.
     *
     * @param factory The event listener factory
     */
    public void setListenerFactory(EventListenerFactory factory) {
        this.listenerFactory = factory;
    }

    /**
     * Set game objects to display.
     *
     * @param gameObjects List of game objects
     */
    public void setGameObjects(ArrayList<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
        updateUIButtons();
        repaint();
    }

    /**
     * Update UI buttons for all clickable game objects.
     * OPTIMIZED: Uses factory to create click listeners.
     */
    private void updateUIButtons() {
        // Remove old buttons
        for (JButton btn : uiButtons.values()) {
            remove(btn);
        }
        uiButtons.clear();
        clickListeners.clear();

        // Create new buttons
        for (GameObject obj : gameObjects) {
            if (!obj.isActive()) continue;

            if (hasOnClickEvent(obj)) {
                createUIButton(obj);
            }
        }

        revalidate();
    }

    /**
     * Create UI button for a clickable GameObject.
     * OPTIMIZED: Uses factory to create ClickListener.
     *
     * @param obj The game object
     */
    private void createUIButton(GameObject obj) {
        Transform t = obj.getTransform();
        if (t == null) return;

        // Create button with GameObject's name as label
        String label = obj.getName();
        JButton button = new JButton(label);

        // Set position and size
        int x = (int) t.getX();
        int y = (int) t.getY();
        int width = 120;
        int height = 40;

        button.setBounds(x, y, width, height);

        // Create ClickListener using Factory (if available)
        ClickListener clickListener;
        if (listenerFactory != null) {
            clickListener = (ClickListener) listenerFactory.createClickListener(label);
        } else {
            // Fallback: create directly if factory not set
            clickListener = new ClickListener(label);
        }
        clickListeners.put(obj, clickListener);

        // Add click action
        button.addActionListener(e -> {
            clickListener.notifyClicked();
            executeOnClickTriggers(obj);
            System.out.println("🖱️ Button clicked: [" + label + "]");
        });

        uiButtons.put(obj, button);
        add(button);
    }

    /**
     * Check if GameObject has OnClickEvent trigger.
     *
     * @param obj The game object
     * @return true if has OnClickEvent
     */
    private boolean hasOnClickEvent(GameObject obj) {
        TriggerManager tm = obj.getTriggerManager();
        if (tm == null) return false;

        for (Trigger trigger : tm.getAllTriggers()) {
            if (trigger.getEvent() instanceof OnClickEvent) {
                return true;
            }
        }
        return false;
    }

    /**
     * Execute all OnClick triggers for a GameObject.
     *
     * @param obj The game object
     */
    private void executeOnClickTriggers(GameObject obj) {
        TriggerManager tm = obj.getTriggerManager();
        if (tm == null) return;

        for (Trigger trigger : tm.getAllTriggers()) {
            if (trigger.getEvent() instanceof OnClickEvent) {
                executeTrigger(trigger, obj);
            }
        }
    }

    /**
     * Execute a single trigger.
     *
     * @param trigger The trigger to execute
     * @param obj The game object
     */
    private void executeTrigger(Trigger trigger, GameObject obj) {
        try {
            Environment localEnv = obj.getEnvironment();

            // Check conditions
            boolean allConditionsMet = true;
            for (Condition condition : trigger.getConditions()) {
                if (!condition.evaluate(globalEnvironment, localEnv)) {
                    allConditionsMet = false;
                    break;
                }
            }

            // Execute actions
            if (allConditionsMet) {
                for (Action action : trigger.getActions()) {
                    action.execute(globalEnvironment, localEnv);
                }
            }
        } catch (Exception e) {
            System.err.println("Error executing trigger: " + e.getMessage());
        }
    }

    /**
     * Get click listener for a GameObject.
     *
     * @param obj The game object
     * @return The click listener, or null if not found
     */
    public ClickListener getClickListener(GameObject obj) {
        return clickListeners.get(obj);
    }

    /**
     * Reset all click listeners.
     * Called after each game loop frame.
     */
    public void resetClickListeners() {
        for (ClickListener listener : clickListeners.values()) {
            listener.reset();
        }
    }

    /**
     * Update positions of clickable UI buttons.
     * Called when transforms change.
     */
    public void updateClickablePositions() {
        for (Map.Entry<GameObject, JButton> entry : uiButtons.entrySet()) {
            GameObject obj = entry.getKey();
            JButton button = entry.getValue();
            Transform t = obj.getTransform();

            if (t != null) {
                button.setLocation((int) t.getX(), (int) t.getY());
            }
        }
    }

    /**
     * Set global environment for triggers.
     *
     * @param env The global environment
     */
    public void setGlobalEnvironment(Environment env) {
        this.globalEnvironment = env;
    }

    /**
     * Set current FPS for display.
     *
     * @param fps The FPS value
     */
    public void setFps(int fps) {
        this.currentFps = fps;
    }

    /**
     * Set whether to show FPS counter.
     *
     * @param show true to show FPS
     */
    public void setShowFps(boolean show) {
        this.showFps = show;
    }

    /**
     * Set debug mode (for future use).
     *
     * @param debug true for debug mode
     */
    public void setDebugMode(boolean debug) {
        // Optional: implement debug visualization
    }

    /**
     * Set whether to show grid (for future use).
     *
     * @param show true to show grid
     */
    public void setShowGrid(boolean show) {
        // Optional: implement grid visualization
    }

    /**
     * Set background color.
     *
     * @param color The background color
     */
    public void setBackgroundColor(Color color) {
        setBackground(color);
    }

    /**
     * Paint component - renders FPS and other info.
     *
     * @param g Graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw FPS counter
        if (showFps) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("FPS: " + currentFps, 10, 20);
        }
    }

    /**
     * Dispose of canvas resources.
     */
    public void dispose() {
        for (JButton btn : uiButtons.values()) {
            remove(btn);
        }
        uiButtons.clear();
        clickListeners.clear();
    }
}