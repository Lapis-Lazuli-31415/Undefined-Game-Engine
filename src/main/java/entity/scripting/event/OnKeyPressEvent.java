package entity.scripting.event;

/**
 * OnKeyPressEvent - Event triggered when a specific key is pressed
 *
 * @author Wanru Cheng
 */
public class OnKeyPressEvent extends Event {

    private final String key;

    /**
     * Create an OnKeyPressEvent
     *
     * @param key The key name (e.g., "W", "Space", "Up", "Enter")
     */
    public OnKeyPressEvent(String key) {
        super("On Key Press");
        this.key = key;
    }

    /**
     * Get the key that triggers this event
     *
     * @return The key name
     */
    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "OnKeyPressEvent[key=" + key + "]";
    }
}