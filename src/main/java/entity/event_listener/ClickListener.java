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
    private final int canvasWidth;
    private final int canvasHeight;

    /**
     * Create a ClickListener for collision-based detection (NEW).
     *
     * @param gameObject The game object to detect clicks on
     * @param inputManager The input manager with mouse state
     * @param canvasWidth The width of the game canvas
     * @param canvasHeight The height of the game canvas
     */
    public ClickListener(GameObject gameObject, InputManager inputManager, int canvasWidth, int canvasHeight) {
        this.gameObject = gameObject;
        this.inputManager = inputManager;
        this.buttonLabel = gameObject != null ? gameObject.getName() : "Unknown";
        this.wasClicked = false;
        this.useCollisionDetection = true;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
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
        this.canvasWidth = 800;  // Default for button mode
        this.canvasHeight = 600;
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
            boolean triggered = isClickedByCollision();

            // Debug output when click detected
            if (inputManager.isLeftClickJustPressed()) {
                int mx = inputManager.getMouseX();
                int my = inputManager.getMouseY();
                System.out.println("🖱️  Click at (" + mx + ", " + my + ") for " + buttonLabel);
                System.out.println("   Triggered: " + triggered);
            }

            return triggered;
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
     * Bounding box calculation matches GameCanvas rendering:
     * - Uses center-based coordinate system
     * - Applies scale from Transform
     * - Uses actual canvas dimensions
     *
     * @param mouseX Mouse X coordinate
     * @param mouseY Mouse Y coordinate
     * @return true if point is inside bounding box
     */
    private boolean isPointInGameObject(int mouseX, int mouseY) {
        Transform transform = gameObject.getTransform();
        if (transform == null) {
            return false;
        }

        SpriteRenderer spriteRenderer = gameObject.getSpriteRenderer();

        // Default size if no sprite
        int width = 50;
        int height = 50;

        // Get actual size if sprite exists
        if (spriteRenderer != null && spriteRenderer.getSprite() != null) {
            width = spriteRenderer.getWidth();
            height = spriteRenderer.getHeight();
        }

        // Apply scale from transform
        int drawW = (int) (width * transform.getScaleX());
        int drawH = (int) (height * transform.getScaleY());

        // Use actual canvas size (matches GameCanvas rendering)
        int panelW = canvasWidth;
        int panelH = canvasHeight;

        // Center-based coordinate calculation (same as GameCanvas)
        int centerX = (panelW - drawW) / 2;
        int centerY = (panelH - drawH) / 2;

        int drawX = centerX + (int) transform.getX();
        int drawY = centerY + (int) transform.getY();

        // Debug output
        System.out.println("   " + buttonLabel + " bounds: [" + drawX + "," + drawY + " to " + (drawX+drawW) + "," + (drawY+drawH) + "]");
        System.out.println("   Canvas size: " + canvasWidth + "x" + canvasHeight);

        // Check if mouse is inside bounds
        boolean inside = mouseX >= drawX && mouseX <= drawX + drawW &&
                mouseY >= drawY && mouseY <= drawY + drawH;

        return inside;
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
}