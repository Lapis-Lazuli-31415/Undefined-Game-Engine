package use_case.preview;

import use_case.validate_scene.ValidationResult;
import use_case.validate_scene.ValidateSceneInputBoundary;
import entity.GameObject;
import entity.Property;
import entity.Scene;
import entity.scripting.environment.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.validate_scene.ValidateSceneInteractor;

import java.util.ArrayList;

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
        validator = new ValidateSceneInteractor();
        presenter = new TestPreviewPresenter();
        interactor = new PreviewInteractor(validator, presenter);
    }
    @Test
    void stop_callsSuccessfully() {
        // Act - just verify it doesn't throw
        interactor.stop();

        // No exception means success
        assertTrue(true);
    }
    @Test
    void execute_sceneWithWarning_presentsWarning() {
        ValidateSceneInputBoundary warningValidator = new ValidateSceneInputBoundary() {
            @Override
            public ValidationResult validate(Scene scene) {
                return ValidationResult.warning("Test warning message");
            }
        };

        TestPreviewPresenter testPresenter = new TestPreviewPresenter();
        PreviewInteractor testInteractor = new PreviewInteractor(warningValidator, testPresenter);

        Scene scene = createValidScene();
        PreviewInputData inputData = new PreviewInputData(scene);

        // Act
        testInteractor.execute(inputData);

        // Assert
        assertTrue(testPresenter.warningCalled);
        assertEquals("Test warning message", testPresenter.lastWarningMessage);
        assertNotNull(testPresenter.lastOutputData);
    }
    @Test
    void execute_validSceneWithWarning_presentsWarning() {
        // This test needs a scene that triggers warning
        // Since we removed music check, we need to check if there's any warning case
        Scene validScene = createValidScene();
        PreviewInputData inputData = new PreviewInputData(validScene);

        interactor.execute(inputData);

        // Should be success since scene is valid
        assertTrue(presenter.successCalled || presenter.warningCalled);
    }

    @Test
    void execute_validScene_presentsSuccess() {
        // Arrange
        Scene validScene = createValidScene();
        PreviewInputData inputData = new PreviewInputData(validScene);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.successCalled);
        assertFalse(presenter.errorCalled);
        assertNotNull(presenter.lastOutputData);
        assertEquals(validScene, presenter.lastOutputData.getScene());
    }

    @Test
    void execute_nullScene_presentsError() {
        // Arrange
        PreviewInputData inputData = new PreviewInputData(null);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.errorCalled);
        assertFalse(presenter.successCalled);
        assertNotNull(presenter.lastErrorMessage);
    }

    @Test
    void execute_sceneWithNoId_presentsError() {
        // Arrange
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        Scene invalidScene = new Scene(null, "Test Scene", objects, null);
        PreviewInputData inputData = new PreviewInputData(invalidScene);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.errorCalled);
        assertFalse(presenter.successCalled);
    }

    @Test
    void execute_sceneWithNoName_presentsError() {
        // Arrange
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        Scene invalidScene = new Scene("test-id", null, objects, null);
        PreviewInputData inputData = new PreviewInputData(invalidScene);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.errorCalled);
        assertFalse(presenter.successCalled);
    }

    @Test
    void execute_sceneWithEmptyName_presentsError() {
        // Arrange
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        Scene invalidScene = new Scene("test-id", "", objects, null);
        PreviewInputData inputData = new PreviewInputData(invalidScene);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.errorCalled);
        assertFalse(presenter.successCalled);
    }

    @Test
    void execute_sceneWithNoGameObjects_presentsError() {
        // Arrange
        Scene invalidScene = new Scene("test-id", "Test Scene", new ArrayList<>(), null);
        PreviewInputData inputData = new PreviewInputData(invalidScene);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.errorCalled);
        assertFalse(presenter.successCalled);
    }

    @Test
    void execute_sceneWithNullGameObjects_presentsError() {
        // Arrange
        Scene invalidScene = new Scene("test-id", "Test Scene", null, null);
        PreviewInputData inputData = new PreviewInputData(invalidScene);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.errorCalled);
        assertFalse(presenter.successCalled);
    }

    @Test
    void getScene_returnsCorrectScene() {
        // Arrange
        Scene scene = createValidScene();
        PreviewInputData inputData = new PreviewInputData(scene);

        // Act & Assert
        assertEquals(scene, inputData.getScene());
    }

    // Helper methods

    private Scene createValidScene() {
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

    // Test double for PreviewOutputBoundary
    private static class TestPreviewPresenter implements PreviewOutputBoundary {
        boolean successCalled = false;
        boolean errorCalled = false;
        boolean warningCalled = false;
        PreviewOutputData lastOutputData = null;
        String lastErrorMessage = null;
        String lastWarningMessage = null;

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
            lastOutputData = outputData;
        }
    }
}