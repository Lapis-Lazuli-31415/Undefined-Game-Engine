package interface_adapter.preview;

import entity.GameObject;
import entity.InputState;
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
 * @author Wanru Cheng
 */
public class EventListenerFactory {

    private final InputState inputState;
    private final boolean useCollisionDetection;

    public EventListenerFactory(InputState inputState, boolean useCollisionDetection) {
        this.inputState = inputState;
        this.useCollisionDetection = useCollisionDetection;
    }

    public EventListenerFactory(InputState inputState) {
        this(inputState, true);
    }

    public EventListener createKeyPressListener(OnKeyPressEvent event) {
        return new KeyPressListener(event, inputState);
    }

    public EventListener createClickListener(GameObject gameObject) {
        if (useCollisionDetection) {
            return new ClickListener(gameObject, inputState);
        } else {
            String buttonLabel = gameObject != null ? gameObject.getName() : "Unknown";
            return new ClickListener(buttonLabel);
        }
    }

    public EventListener createButtonClickListener(String buttonLabel) {
        return new ClickListener(buttonLabel);
    }

    public EventListener createCollisionClickListener(GameObject gameObject) {
        return new ClickListener(gameObject, inputState);
    }

    public EventListener createListener(Event event, GameObject gameObject) {
        if (event instanceof OnKeyPressEvent onKeyPressEvent) {
            return createKeyPressListener(onKeyPressEvent);
        } else if (event instanceof OnClickEvent) {
            return createClickListener(gameObject);
        }

        System.err.println("Unknown event type: " + event.getClass().getName());
        return null;
    }

    public InputState getInputState() {
        return inputState;
    }

    public boolean isUsingCollisionDetection() {
        return useCollisionDetection;
    }
}