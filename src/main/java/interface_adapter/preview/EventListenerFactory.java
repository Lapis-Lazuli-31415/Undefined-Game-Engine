package interface_adapter.preview;

import entity.GameObject;
import entity.InputManager;
import entity.Eventlistener.EventListener;
import entity.Eventlistener.KeyPressListener;
import entity.Eventlistener.ClickListener;
import entity.scripting.event.Event;
import entity.scripting.event.OnKeyPressEvent;
import entity.scripting.event.OnClickEvent;

/**
 * Factory for creating EventListener objects.
 * Part of Interface Adapter layer (green ring in CA diagram).
 *
 * Responsibilities:
 * - Create appropriate EventListener based on Event type
 * - Encapsulate listener creation logic
 * - Allow View layer to get listeners without direct instantiation
 *
 * Design Pattern: Factory Pattern
 *
 * @author Wanru Cheng
 */
public class EventListenerFactory {

    private final InputManager inputManager;

    /**
     * Constructor.
     *
     * @param inputManager The input manager for keyboard events
     */
    public EventListenerFactory(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    /**
     * Create an EventListener for a keyboard event.
     *
     * @param event The OnKeyPressEvent
     * @return KeyPressListener for this event
     */
    public EventListener createKeyPressListener(OnKeyPressEvent event) {
        return new KeyPressListener(event, inputManager);
    }

    /**
     * Create an EventListener for a click event.
     *
     * @param buttonName The name of the button/GameObject
     * @return ClickListener for this button
     */
    public EventListener createClickListener(String buttonName) {
        return new ClickListener(buttonName);
    }

    /**
     * Create an EventListener based on Event type (generic factory method).
     *
     * @param event The event to create listener for
     * @param gameObject The GameObject (needed for OnClickEvent)
     * @return Appropriate EventListener, or null if event type unknown
     */
    public EventListener createListener(Event event, GameObject gameObject) {
        if (event instanceof OnKeyPressEvent) {
            return createKeyPressListener((OnKeyPressEvent) event);
        } else if (event instanceof OnClickEvent) {
            // For click events, use GameObject name as button identifier
            String buttonName = gameObject != null ? gameObject.getName() : "Unknown";
            return createClickListener(buttonName);
        }

        // Unknown event type
        System.err.println("Unknown event type: " + event.getClass().getName());
        return null;
    }
}