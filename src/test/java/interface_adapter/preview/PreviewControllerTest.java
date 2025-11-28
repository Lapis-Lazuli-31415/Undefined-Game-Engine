package interface_adapter.preview;

import entity.GameObject;
import entity.InputManager;
import entity.Scene;
import entity.event_listener.ClickListener;
import entity.event_listener.EventListener;
import entity.event_listener.KeyPressListener;
import entity.scripting.environment.Environment;
import entity.scripting.event.Event;
import entity.scripting.event.OnClickEvent;
import entity.scripting.event.OnKeyPressEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.preview.PreviewInputBoundary;
import use_case.preview.PreviewInputData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for PreviewController.
 *
 * @author Wanru Cheng
 */
class PreviewControllerTest {

    private PreviewController controller;
    private TestPreviewInteractor testInteractor;

    @BeforeEach
    void setUp() {
        testInteractor = new TestPreviewInteractor();
        controller = new PreviewController(testInteractor);
    }

    // Helper method to create OnKeyPressEvent with a key
    private OnKeyPressEvent createKeyPressEvent(String key) {
        OnKeyPressEvent event = new OnKeyPressEvent();
        event.addEventParameter("Key", key);
        return event;
    }

    // ========== PreviewController Tests ==========

    @Test
    void execute_withValidScene_callsInteractor() {
        Scene scene = createTestScene();
        controller.execute(scene);

        assertTrue(testInteractor.executeCalled);
        assertNotNull(testInteractor.lastInputData);
        assertEquals(scene, testInteractor.lastInputData.getScene());
    }

    @Test
    void execute_withNullScene_callsInteractor() {
        controller.execute(null);

        assertTrue(testInteractor.executeCalled);
        assertNull(testInteractor.lastInputData.getScene());
    }

    @Test
    void stop_callsInteractorStop() {
        controller.stop();
        assertTrue(testInteractor.stopCalled);
    }

    // ========== Inner EventListenerFactory Tests ==========

    @Test
    void innerFactory_createKeyPressListener_returnsKeyPressListener() {
        InputManager inputManager = new InputManager();
        PreviewController.EventListenerFactory factory =
                new PreviewController.EventListenerFactory(inputManager);
        OnKeyPressEvent event = createKeyPressEvent("W");

        EventListener listener = factory.createKeyPressListener(event);

        assertNotNull(listener);
        assertTrue(listener instanceof KeyPressListener);
    }

    @Test
    void innerFactory_createClickListener_returnsClickListener() {
        InputManager inputManager = new InputManager();
        PreviewController.EventListenerFactory factory =
                new PreviewController.EventListenerFactory(inputManager);

        EventListener listener = factory.createClickListener("TestButton");

        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
    }

    @Test
    void innerFactory_createListener_withOnKeyPressEvent_returnsKeyPressListener() {
        InputManager inputManager = new InputManager();
        PreviewController.EventListenerFactory factory =
                new PreviewController.EventListenerFactory(inputManager);
        OnKeyPressEvent event = createKeyPressEvent("A");
        GameObject obj = createTestGameObject();

        EventListener listener = factory.createListener(event, obj);

        assertNotNull(listener);
        assertTrue(listener instanceof KeyPressListener);
    }

    @Test
    void innerFactory_createListener_withOnClickEvent_returnsClickListener() {
        InputManager inputManager = new InputManager();
        PreviewController.EventListenerFactory factory =
                new PreviewController.EventListenerFactory(inputManager);
        OnClickEvent event = new OnClickEvent();
        GameObject obj = createTestGameObject();

        EventListener listener = factory.createListener(event, obj);

        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
    }

    @Test
    void innerFactory_createListener_withOnClickEventAndNullGameObject_returnsClickListener() {
        InputManager inputManager = new InputManager();
        PreviewController.EventListenerFactory factory =
                new PreviewController.EventListenerFactory(inputManager);
        OnClickEvent event = new OnClickEvent();

        EventListener listener = factory.createListener(event, null);

        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
    }

    @Test
    void innerFactory_createListener_withUnknownEventType_returnsNull() {
        InputManager inputManager = new InputManager();
        PreviewController.EventListenerFactory factory =
                new PreviewController.EventListenerFactory(inputManager);

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

    // ========== Helper methods ==========

    private Scene createTestScene() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        return new Scene("test-id", "Test Scene", objects);
    }

    private GameObject createTestGameObject() {
        return new GameObject(
                "obj-1",
                "TestObject",
                true,
                new ArrayList<>(),
                new Environment()
        );
    }

    // ========== Test double ==========

    private static class TestPreviewInteractor implements PreviewInputBoundary {
        boolean executeCalled = false;
        boolean stopCalled = false;
        PreviewInputData lastInputData = null;

        @Override
        public void execute(PreviewInputData inputData) {
            executeCalled = true;
            lastInputData = inputData;
        }

        @Override
        public void stop() {
            stopCalled = true;
        }
    }
}