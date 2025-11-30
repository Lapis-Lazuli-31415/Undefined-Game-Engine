package interface_adapter.preview;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for PreviewState.
 *
 * @author Wanru Cheng
 */
class PreviewStateTest {

    @Test
    void constructor_defaultValues() {
        // Act
        PreviewState state = new PreviewState();

        // Assert
        assertNull(state.getSceneId());
        assertNull(state.getSceneName());
        assertEquals(0, state.getGameObjectCount());
        assertNull(state.getError());
        assertNull(state.getWarning());
        assertFalse(state.isReadyToPreview());
    }

    @Test
    void setSceneId_storesSceneId() {
        // Arrange
        PreviewState state = new PreviewState();
        String sceneId = "test-scene-id-123";

        // Act
        state.setSceneId(sceneId);

        // Assert
        assertEquals(sceneId, state.getSceneId());
    }

    @Test
    void setSceneName_storesSceneName() {
        // Arrange
        PreviewState state = new PreviewState();
        String sceneName = "Test Scene";

        // Act
        state.setSceneName(sceneName);

        // Assert
        assertEquals(sceneName, state.getSceneName());
    }

    @Test
    void setGameObjectCount_storesCount() {
        // Arrange
        PreviewState state = new PreviewState();
        int count = 5;

        // Act
        state.setGameObjectCount(count);

        // Assert
        assertEquals(count, state.getGameObjectCount());
    }

    @Test
    void setError_storesError() {
        // Arrange
        PreviewState state = new PreviewState();
        String error = "Test error";

        // Act
        state.setError(error);

        // Assert
        assertEquals(error, state.getError());
    }

    @Test
    void setWarning_storesWarning() {
        // Arrange
        PreviewState state = new PreviewState();
        String warning = "Test warning";

        // Act
        state.setWarning(warning);

        // Assert
        assertEquals(warning, state.getWarning());
    }

    @Test
    void isReadyToPreview_initialState_returnsFalse() {
        // Arrange
        PreviewState state = new PreviewState();

        // Assert
        assertFalse(state.isReadyToPreview());
    }

    @Test
    void setReadyToPreview_true_returnsTrue() {
        // Arrange
        PreviewState state = new PreviewState();

        // Act
        state.setReadyToPreview(true);

        // Assert
        assertTrue(state.isReadyToPreview());
    }

    @Test
    void setReadyToPreview_false_returnsFalse() {
        // Arrange
        PreviewState state = new PreviewState();
        state.setReadyToPreview(true);

        // Act
        state.setReadyToPreview(false);

        // Assert
        assertFalse(state.isReadyToPreview());
    }

    @Test
    void setSceneId_withNull_storesNull() {
        // Arrange
        PreviewState state = new PreviewState();
        state.setSceneId("test-id");

        // Act
        state.setSceneId(null);

        // Assert
        assertNull(state.getSceneId());
    }

    @Test
    void multipleSetters_workIndependently() {
        // Arrange
        PreviewState state = new PreviewState();
        String sceneId = "scene-123";
        String sceneName = "My Scene";
        int count = 10;
        String error = "Error";
        String warning = "Warning";

        // Act
        state.setSceneId(sceneId);
        state.setSceneName(sceneName);
        state.setGameObjectCount(count);
        state.setError(error);
        state.setWarning(warning);
        state.setReadyToPreview(true);

        // Assert
        assertEquals(sceneId, state.getSceneId());
        assertEquals(sceneName, state.getSceneName());
        assertEquals(count, state.getGameObjectCount());
        assertEquals(error, state.getError());
        assertEquals(warning, state.getWarning());
        assertTrue(state.isReadyToPreview());
    }

    @Test
    void setGameObjectCount_withZero_storesZero() {
        // Arrange
        PreviewState state = new PreviewState();

        // Act
        state.setGameObjectCount(0);

        // Assert
        assertEquals(0, state.getGameObjectCount());
    }

    @Test
    void setGameObjectCount_withNegative_storesNegative() {
        // Arrange
        PreviewState state = new PreviewState();

        // Act
        state.setGameObjectCount(-1);

        // Assert
        assertEquals(-1, state.getGameObjectCount());
    }
}