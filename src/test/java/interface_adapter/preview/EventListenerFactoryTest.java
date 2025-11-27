package interface_adapter.preview;

import entity.GameObject;
import entity.InputManager;
import entity.Transform;
import entity.Eventlistener.ClickListener;
import entity.Eventlistener.EventListener;
import entity.Eventlistener.KeyPressListener;
import entity.scripting.environment.Environment;
import entity.scripting.event.OnClickEvent;
import entity.scripting.event.OnKeyPressEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for EventListenerFactory.
 *
 * @author Wanru Cheng
 */
class EventListenerFactoryTest {

    private InputManager inputManager;
    private EventListenerFactory factory;

    @BeforeEach
    void setUp() {
        inputManager = new InputManager();
        factory = new EventListenerFactory(inputManager);
    }
    @Test
    void createListener_withUnknownEventType_returnsNull() {
        // Arrange - create a custom Event that's not OnKeyPress or OnClick
        entity.scripting.event.Event unknownEvent = new entity.scripting.event.Event("Unknown") {
            // Anonymous subclass of Event
        };
        GameObject obj = createTestGameObject();

        // Act
        EventListener listener = factory.createListener(unknownEvent, obj);

        // Assert
        assertNull(listener);
    }
    @Test
    void constructor_defaultCollisionDetection_isTrue() {
        // Assert
        assertTrue(factory.isUsingCollisionDetection());
    }

    @Test
    void constructor_withCollisionDetectionFalse_isFalse() {
        // Arrange
        EventListenerFactory factoryNoCollision = new EventListenerFactory(inputManager, false);

        // Assert
        assertFalse(factoryNoCollision.isUsingCollisionDetection());
    }

    @Test
    void getInputManager_returnsSameInstance() {
        // Assert
        assertSame(inputManager, factory.getInputManager());
    }

    @Test
    void createKeyPressListener_returnsKeyPressListener() {
        // Arrange
        OnKeyPressEvent event = new OnKeyPressEvent("W");

        // Act
        EventListener listener = factory.createKeyPressListener(event);

        // Assert
        assertNotNull(listener);
        assertTrue(listener instanceof KeyPressListener);
    }

    @Test
    void createClickListener_collisionMode_returnsCollisionListener() {
        // Arrange
        GameObject obj = createTestGameObject();

        // Act
        EventListener listener = factory.createClickListener(obj);

        // Assert
        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
        assertTrue(((ClickListener) listener).isUsingCollisionDetection());
    }

    @Test
    void createClickListener_buttonMode_returnsButtonListener() {
        // Arrange
        EventListenerFactory buttonFactory = new EventListenerFactory(inputManager, false);
        GameObject obj = createTestGameObject();

        // Act
        EventListener listener = buttonFactory.createClickListener(obj);

        // Assert
        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
        assertFalse(((ClickListener) listener).isUsingCollisionDetection());
    }

    @Test
    void createButtonClickListener_returnsButtonModeListener() {
        // Act
        EventListener listener = factory.createButtonClickListener("TestButton");

        // Assert
        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
        assertFalse(((ClickListener) listener).isUsingCollisionDetection());
    }

    @Test
    void createCollisionClickListener_returnsCollisionModeListener() {
        // Arrange
        GameObject obj = createTestGameObject();

        // Act
        EventListener listener = factory.createCollisionClickListener(obj);

        // Assert
        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
        assertTrue(((ClickListener) listener).isUsingCollisionDetection());
    }

    @Test
    void createListener_withOnKeyPressEvent_returnsKeyPressListener() {
        // Arrange
        OnKeyPressEvent event = new OnKeyPressEvent("A");
        GameObject obj = createTestGameObject();

        // Act
        EventListener listener = factory.createListener(event, obj);

        // Assert
        assertNotNull(listener);
        assertTrue(listener instanceof KeyPressListener);
    }

    @Test
    void createListener_withOnClickEvent_returnsClickListener() {
        // Arrange
        OnClickEvent event = new OnClickEvent();
        GameObject obj = createTestGameObject();

        // Act
        EventListener listener = factory.createListener(event, obj);

        // Assert
        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
    }

    @Test
    void createClickListener_withNullGameObject_handlesGracefully() {
        // Act
        EventListener listener = factory.createClickListener(null);

        // Assert
        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
    }

    // Helper methods

    private GameObject createTestGameObject() {
        Vector<Double> position = new Vector<>();
        position.add(100.0);
        position.add(100.0);

        Vector<Double> scale = new Vector<>();
        scale.add(1.0);
        scale.add(1.0);

        Transform transform = new Transform(position, 0f, scale);

        GameObject obj = new GameObject(
                "obj-1",
                "TestObject",
                true,
                new ArrayList<>(),
                new Environment()
        );
        obj.setTransform(transform);

        return obj;
    }
}