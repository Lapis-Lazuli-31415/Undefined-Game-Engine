package interface_adapter.preview;

import entity.GameObject;
import entity.Scene;
import entity.scripting.environment.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.preview.PreviewInputData;
import use_case.preview.PreviewInputBoundary;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for PreviewController.
 *
 * @author Wanru Cheng
 */
class PreviewControllerTest {

    private PreviewController controller;
    private TestPreviewInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new TestPreviewInteractor();
        controller = new PreviewController(interactor);
    }

    @Test
    void execute_callsInteractorWithCorrectInputData() {
        // Arrange
        Scene scene = createTestScene();

        // Act
        controller.execute(scene);

        // Assert
        assertTrue(interactor.executeCalled);
        assertNotNull(interactor.lastInputData);
        assertEquals(scene, interactor.lastInputData.getScene());
    }

    @Test
    void execute_withNullScene_stillCallsInteractor() {
        // Act
        controller.execute(null);

        // Assert
        assertTrue(interactor.executeCalled);
        assertNotNull(interactor.lastInputData);
        assertNull(interactor.lastInputData.getScene());
    }

    @Test
    void execute_withValidScene_passesSceneToInteractor() {
        // Arrange
        Scene scene = createTestScene();

        // Act
        controller.execute(scene);

        // Assert
        assertTrue(interactor.executeCalled);
        Scene passedScene = interactor.lastInputData.getScene();
        assertEquals(scene, passedScene);
    }

    @Test
    void execute_multipleScenes_callsInteractorMultipleTimes() {
        // Arrange
        Scene scene1 = createTestScene();
        Scene scene2 = createTestScene();

        // Act
        controller.execute(scene1);
        controller.execute(scene2);

        // Assert
        assertEquals(2, interactor.executeCallCount);
    }

    @Test
    void stop_callsInteractorStop() {
        // Act
        controller.stop();

        // Assert
        assertTrue(interactor.stopCalled);
    }

    private Scene createTestScene() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(new GameObject("obj-1", "TestObject", true, new ArrayList<>(), new Environment()));
        return new Scene(UUID.randomUUID(), "Test Scene", objects);
    }

    private static class TestPreviewInteractor implements PreviewInputBoundary {
        boolean executeCalled = false;
        boolean stopCalled = false;
        int executeCallCount = 0;
        PreviewInputData lastInputData = null;

        @Override
        public void execute(PreviewInputData inputData) {
            executeCalled = true;
            executeCallCount++;
            lastInputData = inputData;
        }

        @Override
        public void stop() {
            stopCalled = true;
        }
    }
}