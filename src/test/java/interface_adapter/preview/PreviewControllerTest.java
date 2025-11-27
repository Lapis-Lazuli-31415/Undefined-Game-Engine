package interface_adapter.preview;

import entity.GameObject;
import entity.InputManager;
import entity.Scene;
import entity.Eventlistener.ClickListener;
import entity.Eventlistener.EventListener;
import entity.Eventlistener.KeyPressListener;
import entity.scripting.environment.Environment;
import entity.scripting.event.Event;
import entity.scripting.event.OnClickEvent;
import entity.scripting.event.OnKeyPressEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.preview.PreviewInputBoundary;
import use_case.preview.PreviewInputData;

import java.util.ArrayList;

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

    // ========== PreviewController Tests ==========

    @Test
    void execute_withValidScene_callsInteractor() {
        // Arrange
        Scene scene = createTestScene();

        // Act
        controller.execute(scene);

        // Assert
        assertTrue(testInteractor.executeCalled);
        assertNotNull(testInteractor.lastInputData);
        assertEquals(scene, testInteractor.lastInputData.getScene());
    }

    @Test
    void execute_withNullScene_callsInteractor() {
        // Act
        controller.execute(null);

        // Assert
        assertTrue(testInteractor.executeCalled);
        assertNull(testInteractor.lastInputData.getScene());
    }

    @Test
    void stop_callsInteractorStop() {
        // Act
        controller.stop();

        // Assert
        assertTrue(testInteractor.stopCalled);
    }

    // ========== Inner EventListenerFactory Tests ==========

    @Test
    void innerFactory_createKeyPressListener_returnsKeyPressListener() {
        // Arrange
        InputManager inputManager = new InputManager();
        PreviewController.EventListenerFactory factory =
                new PreviewController.EventListenerFactory(inputManager);
        OnKeyPressEvent event = new OnKeyPressEvent("W");

        // Act
        EventListener listener = factory.createKeyPressListener(event);

        // Assert
        assertNotNull(listener);
        assertTrue(listener instanceof KeyPressListener);
    }

    @Test
    void innerFactory_createClickListener_returnsClickListener() {
        // Arrange
        InputManager inputManager = new InputManager();
        PreviewController.EventListenerFactory factory =
                new PreviewController.EventListenerFactory(inputManager);

        // Act
        EventListener listener = factory.createClickListener("TestButton");

        // Assert
        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
    }

    @Test
    void innerFactory_createListener_withOnKeyPressEvent_returnsKeyPressListener() {
        // Arrange
        InputManager inputManager = new InputManager();
        PreviewController.EventListenerFactory factory =
                new PreviewController.EventListenerFactory(inputManager);
        OnKeyPressEvent event = new OnKeyPressEvent("A");
        GameObject obj = createTestGameObject();

        // Act
        EventListener listener = factory.createListener(event, obj);

        // Assert
        assertNotNull(listener);
        assertTrue(listener instanceof KeyPressListener);
    }

    @Test
    void innerFactory_createListener_withOnClickEvent_returnsClickListener() {
        // Arrange
        InputManager inputManager = new InputManager();
        PreviewController.EventListenerFactory factory =
                new PreviewController.EventListenerFactory(inputManager);
        OnClickEvent event = new OnClickEvent();
        GameObject obj = createTestGameObject();

        // Act
        EventListener listener = factory.createListener(event, obj);

        // Assert
        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
    }

    @Test
    void innerFactory_createListener_withOnClickEventAndNullGameObject_returnsClickListener() {
        // Arrange
        InputManager inputManager = new InputManager();
        PreviewController.EventListenerFactory factory =
                new PreviewController.EventListenerFactory(inputManager);
        OnClickEvent event = new OnClickEvent();

        // Act
        EventListener listener = factory.createListener(event, null);

        // Assert
        assertNotNull(listener);
        assertTrue(listener instanceof ClickListener);
    }

    @Test
    void innerFactory_createListener_withUnknownEventType_returnsNull() {
        // Arrange
        InputManager inputManager = new InputManager();
        PreviewController.EventListenerFactory factory =
                new PreviewController.EventListenerFactory(inputManager);

        // Create a custom Event that's not OnKeyPress or OnClick
        Event unknownEvent = new Event("Unknown") {};
        GameObject obj = createTestGameObject();

        // Act
        EventListener listener = factory.createListener(unknownEvent, obj);

        // Assert
        assertNull(listener);
    }

    // ========== Helper methods ==========

    private Scene createTestScene() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        return new Scene("test-id", "Test Scene", objects, null);
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