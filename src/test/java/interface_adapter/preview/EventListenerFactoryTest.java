package interface_adapter.preview;

import entity.GameObject;
import entity.InputManager;
import entity.Transform;
import entity.event_listener.ClickListener;
import entity.event_listener.EventListener;
import entity.event_listener.KeyPressListener;
import entity.scripting.environment.Environment;
import entity.scripting.event.Event;
import entity.scripting.event.OnClickEvent;
import entity.scripting.event.OnKeyPressEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    // Helper method to create OnKeyPressEvent with a key
    private OnKeyPressEvent createKeyPressEvent(String key) {
        OnKeyPressEvent event = new OnKeyPressEvent();
        event.addEventParameter("Key", key);
        return event;
    }

    @Test
    void createListener_withUnknownEventType_returnsNull() {
        // Create a custom Event that's not OnKeyPress or OnClick
        Event unknownEvent = new Event("Unknown") {
            @Override
            public boolean isRequiredParameter(String key) {
                return false;
            }

            @Override
            public List<String> getRequiredParameters() {
                return Collections.emptyList();
            }
        };
        GameObject obj = createTestGameObject();

        EventListener listener = factory.createListener(unknownEvent, obj);

        assertNull(listener);
    }

    @Test
    void constructor_defaultCollisionDetection_isTrue() {
        assertTrue(factory.isUsingCollisionDetection());
    }

    @Test
    void constructor_withCollisionDetectionFalse_isFalse() {
        EventListenerFactory factoryNoCollision = new EventListenerFactory(inputManager, false);
        assertFalse(factoryNoCollision.isUsingCollisionDetection());
    }

    @Test
    void getInputManager_returnsSameInstance() {
        assertSame(inputManager, factory.getInputManager());
    }

    @Test
    void createKeyPressListener_returnsKeyPressListener() {
        OnKeyPressEvent event = createKeyPressEvent("W");

        EventListener listener = factory.createKeyPressListener(event);

        assertNotNull(listener);
        assertTrue(listener instanceof KeyPressListener);
    }

    @Test
    void createClickListener_collisionMode_returnsCollisionListener() {
        GameObject obj = createTestGameObject();

        EventListener listener = factory.createClickListener(obj);

        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
        assertTrue(((ClickListener) listener).isUsingCollisionDetection());
    }

    @Test
    void createClickListener_buttonMode_returnsButtonListener() {
        EventListenerFactory buttonFactory = new EventListenerFactory(inputManager, false);
        GameObject obj = createTestGameObject();

        EventListener listener = buttonFactory.createClickListener(obj);

        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
        assertFalse(((ClickListener) listener).isUsingCollisionDetection());
    }

    @Test
    void createButtonClickListener_returnsButtonModeListener() {
        EventListener listener = factory.createButtonClickListener("TestButton");

        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
        assertFalse(((ClickListener) listener).isUsingCollisionDetection());
    }

    @Test
    void createCollisionClickListener_returnsCollisionModeListener() {
        GameObject obj = createTestGameObject();

        EventListener listener = factory.createCollisionClickListener(obj);

        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
        assertTrue(((ClickListener) listener).isUsingCollisionDetection());
    }

    @Test
    void createListener_withOnKeyPressEvent_returnsKeyPressListener() {
        OnKeyPressEvent event = createKeyPressEvent("A");
        GameObject obj = createTestGameObject();

        EventListener listener = factory.createListener(event, obj);

        assertNotNull(listener);
        assertTrue(listener instanceof KeyPressListener);
    }

    @Test
    void createListener_withOnClickEvent_returnsClickListener() {
        OnClickEvent event = new OnClickEvent();
        GameObject obj = createTestGameObject();

        EventListener listener = factory.createListener(event, obj);

        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
    }

    @Test
    void createClickListener_withNullGameObject_handlesGracefully() {
        EventListener listener = factory.createClickListener(null);

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