package interface_adapter.preview;

import app.EventListenerFactory;
import entity.InputState;
import entity.event_listener.ClickListener;
import entity.event_listener.EventListener;
import entity.event_listener.KeyPressListener;
import entity.scripting.event.OnKeyPressEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.InputManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for EventListenerFactory.
 *
 * @author Wanru Cheng
 */
class EventListenerFactoryTest {

    private EventListenerFactory factory;
    private InputManager inputManager;

    @BeforeEach
    void setUp() {
        inputManager = new InputManager();
        factory = new EventListenerFactory(inputManager);
    }

    @Test
    void createClickListener_returnsEventListener() {
        // Act
        EventListener listener = factory.createClickListener(null);

        // Assert
        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
    }

    @Test
    void createKeyPressListener_returnsEventListener() {
        // Arrange - Create a valid OnKeyPressEvent
        OnKeyPressEvent event = new OnKeyPressEvent();

        // Act
        EventListener listener = factory.createKeyPressListener(event);

        // Assert
        assertNotNull(listener);
        assertTrue(listener instanceof KeyPressListener);
    }

    @Test
    void getInputState_returnsInputState() {
        // Act
        InputState inputState = factory.getInputState();

        // Assert
        assertNotNull(inputState);
    }
}