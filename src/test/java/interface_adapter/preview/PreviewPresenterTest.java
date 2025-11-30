package interface_adapter.preview;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.preview.PreviewOutputData;

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
    void presentSuccess_updatesViewModelWithSimpleTypes() {
        // Arrange
        PreviewOutputData outputData = new PreviewOutputData(
                "scene-123",
                "Test Scene",
                5,
                null,
                true
        );

        // Act
        presenter.presentSuccess(outputData);

        // Assert
        PreviewState state = viewModel.getState();
        assertNotNull(state);
        assertEquals("scene-123", state.getSceneId());
        assertEquals("Test Scene", state.getSceneName());
        assertEquals(5, state.getGameObjectCount());
        assertNull(state.getError());
        assertNull(state.getWarning());
        assertTrue(state.isReadyToPreview());
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
        assertFalse(state.isReadyToPreview());
    }

    @Test
    void presentWarning_updatesViewModelWithSceneDataAndWarning() {
        // Arrange
        String warningMessage = "Test warning";
        PreviewOutputData outputData = new PreviewOutputData(
                "scene-456",
                "Warning Scene",
                3,
                warningMessage,
                true
        );

        // Act
        presenter.presentWarning(warningMessage, outputData);

        // Assert
        PreviewState state = viewModel.getState();
        assertNotNull(state);
        assertEquals("scene-456", state.getSceneId());
        assertEquals("Warning Scene", state.getSceneName());
        assertEquals(3, state.getGameObjectCount());
        assertEquals(warningMessage, state.getWarning());
        assertTrue(state.isReadyToPreview());
    }

    @Test
    void presentSuccess_withZeroGameObjects_updatesCorrectly() {
        // Arrange
        PreviewOutputData outputData = new PreviewOutputData(
                "empty-scene",
                "Empty Scene",
                0,
                null,
                true
        );

        // Act
        presenter.presentSuccess(outputData);

        // Assert
        PreviewState state = viewModel.getState();
        assertEquals(0, state.getGameObjectCount());
    }

    @Test
    void presentSuccess_withWarningInOutputData_storesWarning() {
        // Arrange
        PreviewOutputData outputData = new PreviewOutputData(
                "scene-789",
                "Scene With Warning",
                2,
                "Minor issue detected",
                true
        );

        // Act
        presenter.presentSuccess(outputData);

        // Assert
        PreviewState state = viewModel.getState();
        assertEquals("Minor issue detected", state.getWarning());
    }

    @Test
    void presentError_clearsOtherFields() {
        // Arrange
        String errorMessage = "Fatal error";

        // Act
        presenter.presentError(errorMessage);

        // Assert
        PreviewState state = viewModel.getState();
        assertEquals(errorMessage, state.getError());
        assertFalse(state.isReadyToPreview());
        // Other fields should be default values
        assertNull(state.getSceneId());
        assertNull(state.getSceneName());
        assertEquals(0, state.getGameObjectCount());
    }
}