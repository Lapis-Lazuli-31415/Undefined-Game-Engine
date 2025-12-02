package entity.event_listener;

import entity.GameObject;
import entity.Transform;
import entity.InputState;
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
    private final InputState inputState;
    private final boolean useCollisionDetection;

    /**
     * Create a ClickListener for collision-based detection (NEW).
     *
     * @param gameObject The game object to detect clicks on
     * @param inputState The input state interface
     */
    public ClickListener(GameObject gameObject, InputState inputState) {
        this.gameObject = gameObject;
        this.inputState = inputState;
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
        this.inputState = null;
        this.buttonLabel = buttonLabel;
        this.wasClicked = false;
        this.useCollisionDetection = false;
    }

    @Override
    public boolean isTriggered() {
        if (useCollisionDetection) {
            if (inputState == null) {
                return false;
            }

            boolean triggered = isClickedByCollision();

            if (inputState.isLeftClickJustPressed()) {
                int mx = inputState.getMouseX();
                int my = inputState.getMouseY();
                System.out.println("🖱️  Click at (" + mx + ", " + my + ") for " + buttonLabel);
                System.out.println("   Triggered: " + triggered);
            }

            return triggered;
        } else {
            return wasClicked;
        }
    }

    private boolean isClickedByCollision() {
        if (inputState == null || gameObject == null) {
            return false;
        }

        if (!inputState.isLeftClickJustPressed()) {
            return false;
        }

        int mouseX = inputState.getMouseX();
        int mouseY = inputState.getMouseY();

        return isPointInGameObject(mouseX, mouseY);
    }

    private boolean isPointInGameObject(int mouseX, int mouseY) {
        Transform transform = gameObject.getTransform();
        if (transform == null) {
            return false;
        }

        SpriteRenderer spriteRenderer = gameObject.getSpriteRenderer();

        int width = 50;
        int height = 50;

        if (spriteRenderer != null && spriteRenderer.getSprite() != null) {
            width = spriteRenderer.getWidth();
            height = spriteRenderer.getHeight();
        }

        int drawW = (int) (width * transform.getScaleX() / 10);
        int drawH = (int) (height * transform.getScaleY() / 10);

        int panelW = inputState.getCanvasWidth();
        int panelH = inputState.getCanvasHeight();
        System.out.println("   Canvas size from InputState: " + panelW + "x" + panelH);

        int centerX = (panelW - drawW) / 2;
        int centerY = (panelH - drawH) / 2;

        int drawX = centerX + (int) transform.getX();
        int drawY = centerY - (int) transform.getY();

        System.out.println("   " + buttonLabel + " bounds: [" + drawX + "," + drawY + " to " + (drawX+drawW) + "," + (drawY+drawH) + "]");

        boolean inside = mouseX >= drawX && mouseX <= drawX + drawW &&
                mouseY >= drawY && mouseY <= drawY + drawH;

        return inside;
    }

    // ========== BUTTON MODE METHODS (Legacy Support) ==========

    public void notifyClicked() {
        wasClicked = true;
    }

    public void reset() {
        wasClicked = false;
    }

    // ========== COMMON METHODS ==========

    public String getButtonLabel() {
        return buttonLabel;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public boolean isUsingCollisionDetection() {
        return useCollisionDetection;
    }

    @Override
    public String toString() {
        String mode = useCollisionDetection ? "collision" : "button";
        return "ClickListener[" + buttonLabel + ", mode=" + mode + "]";
    }
}