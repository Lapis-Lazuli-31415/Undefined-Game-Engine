package entity.event_listener;

import entity.GameObject;
import entity.Transform;
import entity.InputManager;
import entity.Property;
import entity.SpriteRenderer;

/**
 * ClickListener - Detects mouse clicks on GameObject.
 * Part of Entity layer - contains click detection business rules.
 *
 * Supports TWO detection modes:
 * 1. Button mode: UI button triggers (legacy support)
 * 2. Collision mode: Actual sprite collision detection (new feature)
 *
 * @author Wanru Cheng
 */
public class ClickListener implements EventListener {

    // ===== Common Fields =====
    private final String buttonLabel;

    // ===== Button Mode Fields =====
    private boolean wasClicked;  // Set by UI button

    // ===== Collision Mode Fields =====
    private final GameObject gameObject;
    private final InputManager inputManager;
    private final boolean useCollisionDetection;

    /**
     * Create a ClickListener for collision-based detection (NEW).
     *
     * @param gameObject The game object to detect clicks on
     * @param inputManager The input manager with mouse state
     */
    public ClickListener(GameObject gameObject, InputManager inputManager) {
        this.gameObject = gameObject;
        this.inputManager = inputManager;
        this.buttonLabel = gameObject != null ? gameObject.getName() : "Unknown";
        this.wasClicked = false;
        this.useCollisionDetection = true;
    }

    /**
     * Create a ClickListener for button-based detection (LEGACY).
     *
     * @param buttonLabel The button label (GameObject name)
     */
    public ClickListener(String buttonLabel) {
        this.gameObject = null;
        this.inputManager = null;
        this.buttonLabel = buttonLabel;
        this.wasClicked = false;
        this.useCollisionDetection = false;
    }

    /**
     * Check if GameObject was clicked this frame.
     * Automatically uses the appropriate detection mode.
     *
     * @return true if clicked
     */
    @Override
    public boolean isTriggered() {
        if (useCollisionDetection) {
            // Collision-based detection (sprite clicking)
            return isClickedByCollision();
        } else {
            // Button-based detection (UI button)
            return wasClicked;
        }
    }

    /**
     * Check if clicked using collision detection.
     * Uses mouse position and GameObject's bounding box.
     *
     * @return true if mouse click is inside GameObject's bounding box
     */
    private boolean isClickedByCollision() {
        if (inputManager == null || gameObject == null) {
            return false;
        }

        // Check if mouse was just clicked (left button)
        if (!inputManager.isLeftClickJustPressed()) {
            return false;
        }

        // Get mouse position
        int mouseX = inputManager.getMouseX();
        int mouseY = inputManager.getMouseY();

        // Check collision with GameObject's bounding box
        return isPointInGameObject(mouseX, mouseY);
    }

    /**
     * Check if a point (mouse click) is inside GameObject's bounding box.
     *
     * Bounding box calculation:
     * - Center: (transform.x, transform.y)
     * - Size: from SpriteRenderer.getWidth()/getHeight()
     * - Top-left: (x - width/2, y - height/2)
     * - Bottom-right: (x + width/2, y + height/2)
     *
     * @param pointX Mouse X coordinate
     * @param pointY Mouse Y coordinate
     * @return true if point is inside bounding box
     */
    private boolean isPointInGameObject(int pointX, int pointY) {
        // Get transform (position)
        Transform transform = gameObject.getTransform();
        if (transform == null) {
            return false;
        }

        // Transform returns double
        double centerX = transform.getX();
        double centerY = transform.getY();

        // Get sprite renderer and size
        SpriteRenderer spriteRenderer = getSpriteRenderer();
        double width;
        double height;

        if (spriteRenderer != null) {
            // SpriteRenderer returns float, convert to double
            width = spriteRenderer.getWidth();
            height = spriteRenderer.getHeight();
        } else {
            // No sprite renderer, use default size
            width = 50;
            height = 50;
        }

        return isPointInBounds(pointX, pointY, centerX, centerY, width, height);
    }

    /**
     * Check if point is inside bounds.
     *
     * @param pointX Point X
     * @param pointY Point Y
     * @param centerX Center X
     * @param centerY Center Y
     * @param width Width
     * @param height Height
     * @return true if inside
     */
    private boolean isPointInBounds(int pointX, int pointY,
                                    double centerX, double centerY,
                                    double width, double height) {
        // Calculate bounding box edges
        double left = centerX - width / 2;
        double right = centerX + width / 2;
        double top = centerY - height / 2;
        double bottom = centerY + height / 2;

        // Check if point is inside
        return pointX >= left && pointX <= right &&
                pointY >= top && pointY <= bottom;
    }

    /**
     * Get SpriteRenderer property from GameObject.
     *
     * @return SpriteRenderer or null if not found
     */
    private SpriteRenderer getSpriteRenderer() {
        if (gameObject == null) return null;

        for (Property property : gameObject.getProperties()) {
            if (property instanceof SpriteRenderer) {
                return (SpriteRenderer) property;
            }
        }
        return null;
    }

    // ========== BUTTON MODE METHODS (Legacy Support) ==========

    /**
     * Mark that the button was clicked (called by UI button).
     * Only used in button mode.
     */
    public void notifyClicked() {
        wasClicked = true;
    }

    /**
     * Reset the click state (called each frame).
     * Only used in button mode.
     */
    public void reset() {
        wasClicked = false;
    }

    // ========== COMMON METHODS ==========

    /**
     * Get the button label.
     *
     * @return The button label (GameObject name)
     */
    public String getButtonLabel() {
        return buttonLabel;
    }

    /**
     * Get the GameObject this listener is monitoring.
     * Only available in collision mode.
     *
     * @return The game object, or null if in button mode
     */
    public GameObject getGameObject() {
        return gameObject;
    }

    /**
     * Check if using collision detection.
     *
     * @return true if collision mode, false if button mode
     */
    public boolean isUsingCollisionDetection() {
        return useCollisionDetection;
    }

    @Override
    public String toString() {
        String mode = useCollisionDetection ? "collision" : "button";
        return "ClickListener[" + buttonLabel + ", mode=" + mode + "]";
    }
    // test method

}