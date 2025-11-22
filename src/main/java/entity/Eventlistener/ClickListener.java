package entity.Eventlistener;

/**
 * ClickListener - Checks if a UI button (created from GameObject) is clicked
 *
 * The button label is the GameObject's name.
 *
 * @author Wanru Cheng
 */
public class ClickListener implements EventListener {

    private final String buttonLabel;
    private boolean wasClicked;

    /**
     * Create a ClickListener for a GameObject
     *
     * @param buttonLabel The GameObject's name (used as button label)
     */
    public ClickListener(String buttonLabel) {
        this.buttonLabel = buttonLabel;
        this.wasClicked = false;
    }

    /**
     * Check if the button was clicked
     *
     * @return true if clicked this frame
     */
    @Override
    public boolean isTriggered() {
        return wasClicked;
    }

    /**
     * Mark that the button was clicked (called by UI button)
     */
    public void notifyClicked() {
        wasClicked = true;
    }

    /**
     * Reset the click state (called each frame)
     */
    public void reset() {
        wasClicked = false;
    }

    /**
     * Get the button label
     *
     * @return The button label (GameObject name)
     */
    public String getButtonLabel() {
        return buttonLabel;
    }

    @Override
    public String toString() {
        return "ClickListener[button=" + buttonLabel + "]";
    }
}