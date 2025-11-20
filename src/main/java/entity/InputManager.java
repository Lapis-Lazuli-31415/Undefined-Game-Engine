package entity;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

/**
 * InputManager - Manages keyboard input for the game
 *
 * Features:
 * - Case-insensitive key tracking (W = w)
 * - Distinguishes between "held" and "just pressed"
 * - Supports all keyboard keys
 * - 60 FPS compatible with proper state management
 *
 * Key States:
 * - pressedKeys: Keys currently held down
 * - justPressedKeys: Keys pressed this frame (cleared each frame)
 *
 * @author Wanru Cheng
 */
public class InputManager implements KeyListener {

    private final Set<String> pressedKeys;      // Keys currently held
    private final Set<String> justPressedKeys;  // Keys just pressed this frame

    /**
     * Create an InputManager
     */
    public InputManager() {
        this.pressedKeys = new HashSet<>();
        this.justPressedKeys = new HashSet<>();
    }

    /**
     * Check if a key is currently pressed (held down)
     *
     * Case-insensitive: "W" and "w" are treated the same
     *
     * @param key The key name (case insensitive)
     * @return true if the key is held down
     */
    public boolean isKeyPressed(String key) {
        if (key == null) return false;

        // Normalize to uppercase for comparison
        String normalizedKey = key.toUpperCase().trim();
        return pressedKeys.contains(normalizedKey);
    }

    /**
     * Check if a key was just pressed this frame
     *
     * This returns true only ONCE per key press, even if held down.
     * Perfect for single-trigger actions like jumping or shooting.
     *
     * Case-insensitive: "W" and "w" are treated the same
     *
     * @param key The key name (case insensitive)
     * @return true if the key was pressed this frame
     */
    public boolean isKeyJustPressed(String key) {
        if (key == null) return false;

        // Normalize to uppercase for comparison
        String normalizedKey = key.toUpperCase().trim();
        boolean result = justPressedKeys.contains(normalizedKey);

        // Debug output (can be removed in production)
        if (result) {
            System.out.println("  [InputManager] Key just pressed: " + normalizedKey);
        }

        return result;
    }

    /**
     * Get a normalized key name from KeyEvent
     *
     * Converts KeyEvent key codes to standardized uppercase names:
     * - Letters: A, B, C, etc.
     * - Numbers: 0, 1, 2, etc.
     * - Special: SPACE, ENTER, ESCAPE, etc.
     * - Arrows: UP, DOWN, LEFT, RIGHT
     * - Function: F1, F2, etc.
     *
     * @param e The KeyEvent
     * @return Normalized key name (uppercase)
     */
    private String getKeyName(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Get the key text and normalize it
        String keyText = KeyEvent.getKeyText(keyCode);

        // Convert to uppercase and trim
        String normalized = keyText.toUpperCase().trim();

        // Handle special cases where Java returns unexpected names
        normalized = handleSpecialCases(keyCode, normalized);

        return normalized;
    }

    /**
     * Handle special key code cases
     *
     * Some keys have inconsistent names from KeyEvent.getKeyText(),
     * so we standardize them here.
     */
    private String handleSpecialCases(int keyCode, String defaultName) {
        switch (keyCode) {
            // Space key
            case KeyEvent.VK_SPACE:
                return "SPACE";

            // Enter/Return
            case KeyEvent.VK_ENTER:
                return "ENTER";

            // Escape
            case KeyEvent.VK_ESCAPE:
                return "ESCAPE";

            // Tab
            case KeyEvent.VK_TAB:
                return "TAB";

            // Backspace
            case KeyEvent.VK_BACK_SPACE:
                return "BACKSPACE";

            // Arrow keys
            case KeyEvent.VK_UP:
                return "UP";
            case KeyEvent.VK_DOWN:
                return "DOWN";
            case KeyEvent.VK_LEFT:
                return "LEFT";
            case KeyEvent.VK_RIGHT:
                return "RIGHT";

            // Shift, Control, Alt
            case KeyEvent.VK_SHIFT:
                return "SHIFT";
            case KeyEvent.VK_CONTROL:
                return "CONTROL";
            case KeyEvent.VK_ALT:
                return "ALT";

            // Default: use the provided name
            default:
                return defaultName;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        String key = getKeyName(e);

        // If this is a new key press (not held from before)
        if (!pressedKeys.contains(key)) {
            justPressedKeys.add(key);
            System.out.println("[InputManager] Key pressed: " + key);
        }

        // Add to currently pressed keys
        pressedKeys.add(key);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        String key = getKeyName(e);

        // Remove from pressed keys
        pressedKeys.remove(key);

        System.out.println("[InputManager] Key released: " + key);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used, but required by KeyListener interface
    }

    /**
     * Update the input state
     *
     * MUST be called once per frame at the end of the game loop
     * to clear "just pressed" states for the next frame
     */
    public void update() {
        // Clear just pressed keys for next frame
        justPressedKeys.clear();
    }

    /**
     * Reset all input state
     *
     * Useful when switching scenes or pausing
     */
    public void reset() {
        pressedKeys.clear();
        justPressedKeys.clear();
        System.out.println("[InputManager] Input state reset");
    }

    /**
     * Get debug information about current input state
     */
    public String getDebugInfo() {
        return String.format(
                "Pressed: %d keys, Just Pressed: %d keys",
                pressedKeys.size(),
                justPressedKeys.size()
        );
    }
}