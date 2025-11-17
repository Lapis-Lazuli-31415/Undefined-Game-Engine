package entity.Eventlistener;

import app.InputManager;
import entity.scripting.event.OnKeyPressEvent;

/**
 * KeyPressListener - Checks if a specific key is pressed
 *
 * @author Wanru Cheng
 */
public class KeyPressListener implements EventListener {

    private final String key;
    private final InputManager inputManager;

    /**
     * Create a KeyPressListener for OnKeyPressEvent
     *
     * @param event The OnKeyPressEvent to listen for
     * @param inputManager The InputManager instance
     */
    public KeyPressListener(OnKeyPressEvent event, InputManager inputManager) {
        this.key = event.getKey();
        this.inputManager = inputManager;
    }

    /**
     * Check if the key is currently pressed
     *
     * @return true if the key is pressed
     */
    @Override
    public boolean isTriggered() {
        return inputManager.isKeyPressed(key);
    }

    /**
     * Get the key this listener is monitoring
     *
     * @return The key name
     */
    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "KeyPressListener[key=" + key + "]";
    }
}