package view;

import entity.GameObject;
import entity.Transform;
import entity.SpriteRenderer;
import entity.Property;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.event.OnClickEvent;
import entity.Eventlistener.ClickListener;
import entity.scripting.environment.Environment;
import interface_adapter.preview.EventListenerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * GameCanvas - Renders game objects and handles click detection.
 * Part of View layer (blue ring in CA diagram).
 *
 * Supports TWO rendering modes:
 * 1. Button mode: Creates UI buttons (legacy)
 * 2. Sprite mode: Renders actual sprites with collision detection (new)
 *
 * @author Wanru Cheng
 */
public class GameCanvas extends JPanel {

    private ArrayList<GameObject> gameObjects;
    private Map<GameObject, JButton> uiButtons;  // For button mode
    private Map<GameObject, ClickListener> clickListeners;
    private Environment globalEnvironment;
    private EventListenerFactory listenerFactory;

    private int currentFps = 0;
    private boolean showFps = true;
    private boolean showBoundingBoxes = false;
    private boolean useButtonMode = false;  // Default: use sprite mode

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
     *
     * @param factory The event listener factory
     */
    public void setListenerFactory(EventListenerFactory factory) {
        this.listenerFactory = factory;
    }

    /**
     * Set whether to use button mode (true) or sprite mode (false).
     *
     * @param useButtons true for button mode, false for sprite mode
     */
    public void setUseButtonMode(boolean useButtons) {
        this.useButtonMode = useButtons;
    }

    /**
     * Set game objects to display.
     *
     * @param gameObjects List of game objects
     */
    public void setGameObjects(ArrayList<GameObject> gameObjects) {
        this.gameObjects = gameObjects;

        // Don't create UI buttons here anymore - they will be in the status panel
        // Clear any existing buttons
        for (JButton btn : uiButtons.values()) {
            remove(btn);
        }
        uiButtons.clear();
        clickListeners.clear();

        // But still create click listeners for button mode
        if (useButtonMode) {
            for (GameObject obj : gameObjects) {
                if (!obj.isActive()) continue;
                if (hasOnClickEvent(obj)) {
                    createClickListener(obj);
                }
            }
        }

        repaint();
    }

    /**
     * Create ClickListener for a GameObject (without creating UI button).
     */
    private void createClickListener(GameObject obj) {
        String label = obj.getName();

        ClickListener clickListener;
        if (listenerFactory != null) {
            clickListener = (ClickListener) listenerFactory.createButtonClickListener(label);
        } else {
            clickListener = new ClickListener(label);
        }
        clickListeners.put(obj, clickListener);
    }

    /**
     * Update UI buttons for all clickable game objects (BUTTON MODE).
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
     * Create UI button for a clickable GameObject (BUTTON MODE).
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

        button.setBounds(x - width/2, y - height/2, width, height);

        // Create ClickListener using Factory
        ClickListener clickListener;
        if (listenerFactory != null) {
            clickListener = (ClickListener) listenerFactory.createButtonClickListener(label);
        } else {
            clickListener = new ClickListener(label);
        }
        clickListeners.put(obj, clickListener);

        // Add click action
        button.addActionListener(e -> {
            clickListener.notifyClicked();
            System.out.println("Button clicked: [" + label + "]");
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
     * Get click listener for a GameObject (BUTTON MODE).
     *
     * @param obj The game object
     * @return The click listener, or null if not found
     */
    public ClickListener getClickListener(GameObject obj) {
        return clickListeners.get(obj);
    }

    /**
     * Reset all click listeners (BUTTON MODE).
     */
    public void resetClickListeners() {
        for (ClickListener listener : clickListeners.values()) {
            if (!listener.isUsingCollisionDetection()) {
                listener.reset();
            }
        }
    }

    /**
     * Update positions of clickable UI buttons (BUTTON MODE).
     */
    public void updateClickablePositions() {
        for (Map.Entry<GameObject, JButton> entry : uiButtons.entrySet()) {
            GameObject obj = entry.getKey();
            JButton button = entry.getValue();
            Transform t = obj.getTransform();

            if (t != null) {
                button.setLocation((int) (t.getX() - 60), (int) (t.getY() - 20));
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
     * Set whether to show bounding boxes (debug mode).
     *
     * @param show true to show bounding boxes
     */
    public void setShowBoundingBoxes(boolean show) {
        this.showBoundingBoxes = show;
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
     * Paint component - renders game objects and debug info.
     *
     * @param g Graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Only render sprites in sprite mode
        if (!useButtonMode) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            renderGameObjects(g2d);
        }

        // Draw FPS counter (both modes)
        if (showFps) {
            drawFps(g);
        }
    }

    /**
     * Render all game objects (SPRITE MODE).
     *
     * @param g2d Graphics2D context
     */
    private void renderGameObjects(Graphics2D g2d) {
        for (GameObject obj : gameObjects) {
            if (!obj.isActive()) continue;
            renderGameObject(g2d, obj);
        }
    }

    /**
     * Render a single game object (SPRITE MODE).
     *
     * @param g2d Graphics2D context
     * @param obj The game object
     */
    private void renderGameObject(Graphics2D g2d, GameObject obj) {
        Transform transform = obj.getTransform();
        if (transform == null) return;

        SpriteRenderer spriteRenderer = getSpriteRenderer(obj);

        // Get position and size
        double centerX = transform.getX();
        double centerY = transform.getY();
        double width = spriteRenderer != null ? spriteRenderer.getWidth() : 50;
        double height = spriteRenderer != null ? spriteRenderer.getHeight() : 50;

        // Calculate top-left corner
        int x = (int) (centerX - width / 2);
        int y = (int) (centerY - height / 2);

        // Draw sprite or placeholder
        if (spriteRenderer != null && spriteRenderer.getSprite() != null && spriteRenderer.getVisible()) {
            // Draw actual sprite image
            g2d.drawImage(spriteRenderer.getSprite().getBufferedImage(), x, y, (int) width, (int) height, null);
        } else {
            // Draw placeholder
            g2d.setColor(Color.GRAY);
            g2d.fillRect(x, y, (int) width, (int) height);
        }

        // Draw bounding box (debug mode)
        if (showBoundingBoxes) {
            g2d.setColor(Color.GREEN);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(x, y, (int) width, (int) height);
        }

        // Draw object name
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString(obj.getName(), x, y - 5);
    }

    /**
     * Get SpriteRenderer property from GameObject.
     *
     * @param obj The game object
     * @return SpriteRenderer or null if not found
     */
    private SpriteRenderer getSpriteRenderer(GameObject obj) {
        for (Property property : obj.getProperties()) {
            if (property instanceof SpriteRenderer) {
                return (SpriteRenderer) property;
            }
        }
        return null;
    }

    /**
     * Draw FPS counter.
     *
     * @param g Graphics context
     */
    private void drawFps(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("FPS: " + currentFps, 10, 20);

        String mode = useButtonMode ? "Button Mode" : "Sprite Mode";
        g.drawString(mode, 10, 40);

        if (showBoundingBoxes) {
            g.drawString("[Debug: Bounding Boxes]", 10, 60);
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
        gameObjects.clear();
    }
    /**
     * Check if using button mode.
     *
     * @return true if button mode, false if sprite/collision mode
     */
    public boolean isUsingButtonMode() {
        return useButtonMode;
    }
}