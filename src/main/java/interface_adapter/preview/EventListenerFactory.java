package interface_adapter.preview;

import entity.GameObject;
import entity.InputManager;
import entity.event_listener.EventListener;
import entity.event_listener.KeyPressListener;
import entity.event_listener.ClickListener;
import entity.scripting.event.Event;
import entity.scripting.event.OnKeyPressEvent;
import entity.scripting.event.OnClickEvent;

/**
 * Factory for creating EventListener objects.
 * Part of Interface Adapter layer (green ring in CA diagram).
 *
 * Responsibilities:
 * - Create appropriate EventListener based on Event type
 * - Support both button-based and collision-based ClickListeners
 * - Encapsulate listener creation logic
 *
 * Design Pattern: Factory Pattern
 *
 * @author Wanru Cheng
 */
public class EventListenerFactory {

    private final InputManager inputManager;
    private final boolean useCollisionDetection;

    /**
     * Constructor with collision detection option.
     *
     * @param inputManager The input manager for keyboard and mouse events
     * @param useCollisionDetection true to use collision-based clicks, false for buttons
     */
    public EventListenerFactory(InputManager inputManager, boolean useCollisionDetection) {
        this.inputManager = inputManager;
        this.useCollisionDetection = useCollisionDetection;
    }

    /**
     * Constructor (defaults to collision detection enabled).
     *
     * @param inputManager The input manager for keyboard and mouse events
     */
    public EventListenerFactory(InputManager inputManager) {
        this(inputManager, true);  // Default: use collision detection
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
     * Mode depends on factory configuration.
     *
     * @param gameObject The GameObject to detect clicks on
     * @return ClickListener (collision or button mode)
     */
    public EventListener createClickListener(GameObject gameObject) {
        if (useCollisionDetection) {
            // Collision-based detection (new feature)
            return new ClickListener(gameObject, inputManager);
        } else {
            // Button-based detection (legacy)
            String buttonLabel = gameObject != null ? gameObject.getName() : "Unknown";
            return new ClickListener(buttonLabel);
        }
    }

    /**
     * Create a button-based ClickListener (legacy support).
     *
     * @param buttonLabel The button label
     * @return ClickListener in button mode
     */
    public EventListener createButtonClickListener(String buttonLabel) {
        return new ClickListener(buttonLabel);
    }

    /**
     * Create a collision-based ClickListener (new feature).
     *
     * @param gameObject The GameObject
     * @return ClickListener in collision mode
     */
    public EventListener createCollisionClickListener(GameObject gameObject) {
        return new ClickListener(gameObject, inputManager);
    }

    /**
     * Create an EventListener based on Event type (generic factory method).
     *
     * @param event The event to create listener for
     * @param gameObject The GameObject (needed for OnClickEvent)
     * @return Appropriate EventListener, or null if event type unknown
     */
    public EventListener createListener(Event event, GameObject gameObject) {
        if (event instanceof OnKeyPressEvent onKeyPressEvent) {
            return createKeyPressListener(onKeyPressEvent);
        } else if (event instanceof OnClickEvent) {
            return createClickListener(gameObject);
        }

        // Unknown event type
        System.err.println("Unknown event type: " + event.getClass().getName());
        return null;
    }

    /**
     * Get the InputManager.
     *
     * @return The input manager
     */
    public InputManager getInputManager() {
        return inputManager;
    }

    /**
     * Check if using collision detection.
     *
     * @return true if collision mode enabled
     */
    public boolean isUsingCollisionDetection() {
        return useCollisionDetection;
    }
}