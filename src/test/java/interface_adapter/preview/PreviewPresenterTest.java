package interface_adapter.preview;

import entity.GameObject;
import entity.Scene;
import entity.scripting.environment.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.preview.PreviewOutputData;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for PreviewPresenter.
 *
 * @author Wanru Cheng
 */
class PreviewPresenterTest {

    private PreviewPresenter presenter;
    private PreviewViewModel viewModel;

    @BeforeEach
    void setUp() {
        viewModel = new PreviewViewModel();
        presenter = new PreviewPresenter(viewModel);
    }

    @Test
    void presentSuccess_updatesViewModelWithScene() {
        // Arrange
        Scene scene = createTestScene();
        PreviewOutputData outputData = new PreviewOutputData(scene, null);

        // Act
        presenter.presentSuccess(outputData);

        // Assert
        PreviewState state = viewModel.getState();
        assertNotNull(state);
        assertEquals(scene, state.getScene());
        assertNull(state.getError());
    }

    @Test
    void presentError_updatesViewModelWithError() {
        // Arrange
        String errorMessage = "Test error message";

        // Act
        presenter.presentError(errorMessage);

        // Assert
        PreviewState state = viewModel.getState();
        assertNotNull(state);
        assertEquals(errorMessage, state.getError());
    }

    @Test
    void presentWarning_updatesViewModelWithSceneAndWarning() {
        // Arrange
        Scene scene = createTestScene();
        String warningMessage = "Test warning";
        PreviewOutputData outputData = new PreviewOutputData(scene, warningMessage);

        // Act
        presenter.presentWarning(warningMessage, outputData);

        // Assert
        PreviewState state = viewModel.getState();
        assertNotNull(state);
        assertEquals(scene, state.getScene());
        assertEquals(warningMessage, state.getWarning());
    }

    // Helper method
    private Scene createTestScene() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(new GameObject("obj-1", "TestObject", true, new ArrayList<>(), new Environment()));
        return new Scene("test-id", "Test Scene", objects, null);
    }
}