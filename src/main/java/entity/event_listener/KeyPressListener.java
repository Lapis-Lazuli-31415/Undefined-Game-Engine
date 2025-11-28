package entity.event_listener;

import entity.InputManager;
import entity.scripting.event.OnKeyPressEvent;

/**
 * KeyPressListener - Detects keyboard input for specific keys
 *
 * Features:
 * - Case-insensitive key matching (W = w)
 * - Supports all keyboard keys (letters, numbers, special keys)
 * - Single trigger mode (triggers once per key press)
 *
 * Supported keys:
 * - Letters: A-Z (case insensitive)
 * - Numbers: 0-9
 * - Special: Space, Enter, Escape, Tab, Backspace
 * - Arrows: Up, Down, Left, Right
 * - Function: F1-F12
 * - Modifiers: Shift, Control, Alt
 *
 * @author Wanru Cheng
 */
public class KeyPressListener implements EventListener {

    private final String normalizedKey;
    private final InputManager inputManager;

    /**
     * Create a KeyPressListener
     *
     * @param event The OnKeyPressEvent to listen for
     * @param inputManager The InputManager instance
     */
    public KeyPressListener(OnKeyPressEvent event, InputManager inputManager) {
        // Normalize the key: convert to uppercase and trim whitespace
        this.normalizedKey = normalizeKey(event.getEventParameter("Key"));        this.inputManager = inputManager;

        System.out.println("  [KeyPressListener] Created for key: '" +
                event.getKey() + "' → normalized: '" + normalizedKey + "'");
    }

    /**
     * Normalize a key name for consistent matching
     *
     * Rules:
     * - Convert to uppercase
     * - Trim whitespace
     * - Handle common variations
     *
     * Examples:
     * - "w" → "W"
     * - "W" → "W"
     * - " space " → "SPACE"
     * - "up" → "UP"
     *
     * @param key The raw key name
     * @return Normalized key name
     */
    private String normalizeKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            System.err.println("[KeyPressListener] Warning: Empty key name, using 'UNKNOWN'");
            return "UNKNOWN";
        }

        // Convert to uppercase and trim
        String normalized = key.trim().toUpperCase();

        // Handle common variations
        normalized = handleKeyVariations(normalized);

        return normalized;
    }

    /**
     * Handle common key name variations
     *
     * Maps alternative names to standard names:
     * - "SPACEBAR" → "SPACE"
     * - "ESC" → "ESCAPE"
     * - "CTRL" → "CONTROL"
     * - etc.
     */
    private String handleKeyVariations(String key) {
        switch (key) {
            // Space variations
            case "SPACEBAR":
            case "SPACE BAR":
                return "SPACE";

            // Escape variations
            case "ESC":
                return "ESCAPE";

            // Control variations
            case "CTRL":
            case "CTL":
                return "CONTROL";

            // Alt variations
            case "OPTION":  // Mac keyboard
                return "ALT";

            // Arrow key variations
            case "UPARROW":
            case "UP ARROW":
                return "UP";
            case "DOWNARROW":
            case "DOWN ARROW":
                return "DOWN";
            case "LEFTARROW":
            case "LEFT ARROW":
                return "LEFT";
            case "RIGHTARROW":
            case "RIGHT ARROW":
                return "RIGHT";

            // Enter variations
            case "RETURN":
                return "ENTER";

            // Default: no change
            default:
                return key;
        }
    }

    /**
     * Check if the key was just pressed this frame
     *
     * Uses case-insensitive matching with normalized key names
     *
     * @return true if the key was just pressed (single trigger)
     */
    @Override
    public boolean isTriggered() {
        // Check with normalized key
        return inputManager.isKeyJustPressed(normalizedKey);
    }

    /**
     * Get the normalized key this listener is monitoring
     *
     * @return The normalized key name (uppercase)
     */
    public String getKey() {
        return normalizedKey;
    }

    @Override
    public String toString() {
        return "KeyPressListener[key=" + normalizedKey + "]";
    }
}