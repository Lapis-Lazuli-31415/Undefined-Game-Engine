package entity.event_listener;

import entity.InputState;
import entity.scripting.event.OnKeyPressEvent;

/**
 * KeyPressListener - Detects keyboard input for specific keys
 *
 * @author Wanru Cheng
 */
public class KeyPressListener implements EventListener {

    private final String normalizedKey;
    private final InputState inputState;

    public KeyPressListener(OnKeyPressEvent event, InputState inputState) {
        this.normalizedKey = normalizeKey(event.getEventParameter("Key"));
        this.inputState = inputState;

        System.out.println("  [KeyPressListener] Created for key: '" +
                event.getKey() + "' → normalized: '" + normalizedKey + "'");
    }

    private String normalizeKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            System.err.println("[KeyPressListener] Warning: Empty key name, using 'UNKNOWN'");
            return "UNKNOWN";
        }

        String normalized = key.trim().toUpperCase();
        normalized = handleKeyVariations(normalized);

        return normalized;
    }

    private String handleKeyVariations(String key) {
        switch (key) {
            case "SPACEBAR":
            case "SPACE BAR":
                return "SPACE";
            case "ESC":
                return "ESCAPE";
            case "CTRL":
            case "CTL":
                return "CONTROL";
            case "OPTION":
                return "ALT";
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
            case "RETURN":
                return "ENTER";
            default:
                return key;
        }
    }

    @Override
    public boolean isTriggered() {
        return inputState.isKeyJustPressed(normalizedKey);
    }

    public String getKey() {
        return normalizedKey;
    }

    @Override
    public String toString() {
        return "KeyPressListener[key=" + normalizedKey + "]";
    }
}