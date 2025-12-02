package use_case.preview;

import entity.GameObject;
import entity.Scene;
import entity.Transform;
import entity.scripting.TriggerManager;
import entity.scripting.environment.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.validate_scene.ValidateSceneInteractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for PreviewInteractor.
 *
 * @author Wanru Cheng
 */
class PreviewInteractorTest {

    private PreviewInteractor interactor;
    private TestPreviewPresenter presenter;
    private ValidateSceneInteractor validator;

    @BeforeEach
    void setUp() {
        presenter = new TestPreviewPresenter();
        validator = new ValidateSceneInteractor();
        interactor = new PreviewInteractor(validator, presenter);
    }

    @Test
    void execute_withValidScene_callsPresentSuccess() {
        // Arrange
        Scene scene = createValidScene();
        PreviewInputData inputData = new PreviewInputData(scene);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.successCalled);
        assertFalse(presenter.errorCalled);
        assertNotNull(presenter.lastOutputData);
        assertEquals(scene.getId().toString(), presenter.lastOutputData.getSceneId());
        assertEquals(scene.getName(), presenter.lastOutputData.getSceneName());
        assertEquals(1, presenter.lastOutputData.getGameObjectCount());
    }

    @Test
    void execute_withNullScene_callsPresentError() {
        // Arrange
        PreviewInputData inputData = new PreviewInputData(null);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.errorCalled);
        assertFalse(presenter.successCalled);
        assertEquals("Scene is null", presenter.lastErrorMessage);
    }

    @Test
    void execute_withSceneWithNullId_callsPresentError() {
        // Arrange
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        Scene scene = new Scene(null, "Test Scene", objects);
        PreviewInputData inputData = new PreviewInputData(scene);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.errorCalled);
        assertFalse(presenter.successCalled);
        assertEquals("Scene ID needed", presenter.lastErrorMessage);
    }

    @Test
    void execute_withSceneWithNullName_callsPresentError() {
        // Arrange
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        Scene scene = new Scene(UUID.randomUUID(), null, objects);
        PreviewInputData inputData = new PreviewInputData(scene);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.errorCalled);
        assertFalse(presenter.successCalled);
        assertEquals("Scene Name needed", presenter.lastErrorMessage);
    }

    @Test
    void execute_withSceneWithEmptyName_callsPresentError() {
        // Arrange
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        Scene scene = new Scene(UUID.randomUUID(), "", objects);
        PreviewInputData inputData = new PreviewInputData(scene);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.errorCalled);
        assertFalse(presenter.successCalled);
        assertEquals("Scene Name needed", presenter.lastErrorMessage);
    }

    @Test
    void execute_withSceneWithNullGameObjects_callsPresentError() {
        // Arrange
        Scene scene = new Scene(UUID.randomUUID(), "Test Scene", null);
        PreviewInputData inputData = new PreviewInputData(scene);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.errorCalled);
        assertFalse(presenter.successCalled);
        assertEquals("GameObjects needed", presenter.lastErrorMessage);
    }

    @Test
    void execute_withSceneWithEmptyGameObjects_callsPresentError() {
        // Arrange
        Scene scene = new Scene(UUID.randomUUID(), "Test Scene", new ArrayList<>());
        PreviewInputData inputData = new PreviewInputData(scene);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.errorCalled);
        assertFalse(presenter.successCalled);
        assertEquals("GameObjects needed", presenter.lastErrorMessage);
    }

    @Test
    void execute_withSceneWithMultipleGameObjects_callsPresentSuccess() {
        // Arrange
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        objects.add(new GameObject("obj-2", "Object2", true, new Environment(), createDefaultTransform(), null, new TriggerManager()));
        objects.add(new GameObject("obj-3", "Object3", true, new Environment(), createDefaultTransform(), null, new TriggerManager()));
        Scene scene = new Scene(UUID.randomUUID(), "Test Scene", objects);
        PreviewInputData inputData = new PreviewInputData(scene);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.successCalled);
        assertFalse(presenter.errorCalled);
        assertEquals(3, presenter.lastOutputData.getGameObjectCount());
    }

    @Test
    void execute_extractsSceneIdCorrectly() {
        // Arrange
        UUID sceneId = UUID.randomUUID();
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        Scene scene = new Scene(sceneId, "Test Scene", objects);
        PreviewInputData inputData = new PreviewInputData(scene);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.successCalled);
        assertEquals(sceneId.toString(), presenter.lastOutputData.getSceneId());
    }

    @Test
    void execute_extractsSceneNameCorrectly() {
        // Arrange
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        Scene scene = new Scene(UUID.randomUUID(), "My Awesome Scene", objects);
        PreviewInputData inputData = new PreviewInputData(scene);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.successCalled);
        assertEquals("My Awesome Scene", presenter.lastOutputData.getSceneName());
    }

    @Test
    void execute_extractsGameObjectCountCorrectly() {
        // Arrange
        ArrayList<GameObject> objects = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            objects.add(createTestGameObject());
        }
        Scene scene = new Scene(UUID.randomUUID(), "Test Scene", objects);
        PreviewInputData inputData = new PreviewInputData(scene);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.successCalled);
        assertEquals(5, presenter.lastOutputData.getGameObjectCount());
    }

    @Test
    void stop_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> interactor.stop());
    }

    @Test
    void inputData_getterReturnsCorrectScene() {
        // Arrange
        Scene scene = createValidScene();
        PreviewInputData inputData = new PreviewInputData(scene);

        // Assert
        assertEquals(scene, inputData.getScene());
    }

    @Test
    void execute_errorDoesNotAccessSceneProperties() {
        // Arrange - null scene will cause error
        PreviewInputData inputData = new PreviewInputData(null);

        // Act - should not throw NullPointerException
        assertDoesNotThrow(() -> interactor.execute(inputData));

        // Assert
        assertTrue(presenter.errorCalled);
        assertNull(presenter.lastOutputData);
    }

    // ===== HELPER METHODS =====

    private Scene createValidScene() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        return new Scene(UUID.randomUUID(), "Test Scene", objects);
    }

    private GameObject createTestGameObject() {
        return new GameObject(
                "obj-1",
                "TestObject",
                true,
                new Environment(),
                createDefaultTransform(),
                null,
                new TriggerManager()
        );
    }

    private Transform createDefaultTransform() {
        Vector<Double> position = new Vector<>(Arrays.asList(0.0, 0.0));
        Vector<Double> scale = new Vector<>(Arrays.asList(1.0, 1.0));
        return new Transform(position, 0f, scale);
    }

    // ===== TEST HELPERS =====

    private static class TestPreviewPresenter implements PreviewOutputBoundary {
        boolean successCalled = false;
        boolean errorCalled = false;
        boolean warningCalled = false;
        PreviewOutputData lastOutputData = null;
        String lastErrorMessage = null;
        String lastWarningMessage = null;
        PreviewOutputData lastWarningData = null;

        @Override
        public void presentSuccess(PreviewOutputData outputData) {
            successCalled = true;
            lastOutputData = outputData;
        }

        @Override
        public void presentError(String errorMessage) {
            errorCalled = true;
            lastErrorMessage = errorMessage;
        }

        @Override
        public void presentWarning(String warningMessage, PreviewOutputData outputData) {
            warningCalled = true;
            lastWarningMessage = warningMessage;
            lastWarningData = outputData;
        }
    }
    @Test
    void outputData_gettersReturnCorrectValues() {
        // Test all PreviewOutputData getters
        PreviewOutputData outputData = new PreviewOutputData(
                "scene-123",
                "Test Scene",
                5,
                "This is a warning",
                true
        );

        assertEquals("scene-123", outputData.getSceneId());
        assertEquals("Test Scene", outputData.getSceneName());
        assertEquals(5, outputData.getGameObjectCount());
        assertEquals("This is a warning", outputData.getWarning());
        assertTrue(outputData.isValid());
    }

    @Test
    void outputData_withNullWarning_returnsNull() {
        // Test with null warning
        PreviewOutputData outputData = new PreviewOutputData(
                "scene-456",
                "Another Scene",
                3,
                null,
                true
        );

        assertNull(outputData.getWarning());
        assertTrue(outputData.isValid());
    }

    @Test
    void outputData_withInvalidFlag_returnsFalse() {
        // Test with isValid = false
        PreviewOutputData outputData = new PreviewOutputData(
                "scene-789",
                "Invalid Scene",
                0,
                "Error message",
                false
        );

        assertFalse(outputData.isValid());
        assertEquals("Error message", outputData.getWarning());
    }
}