package interface_adapter.preview;

import entity.GameObject;
import entity.Scene;
import entity.scripting.environment.Environment;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
        assertNull(state.getScene());
        assertNull(state.getError());
        assertNull(state.getWarning());
    }

    @Test
    void setScene_storesScene() {
        // Arrange
        PreviewState state = new PreviewState();
        Scene scene = createTestScene();

        // Act
        state.setScene(scene);

        // Assert
        assertEquals(scene, state.getScene());
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
    void setScene_withNull_storesNull() {
        // Arrange
        PreviewState state = new PreviewState();
        state.setScene(createTestScene());

        // Act
        state.setScene(null);

        // Assert
        assertNull(state.getScene());
    }

    @Test
    void multipleSetters_workIndependently() {
        // Arrange
        PreviewState state = new PreviewState();
        Scene scene = createTestScene();
        String error = "Error";
        String warning = "Warning";

        // Act
        state.setScene(scene);
        state.setError(error);
        state.setWarning(warning);

        // Assert
        assertEquals(scene, state.getScene());
        assertEquals(error, state.getError());
        assertEquals(warning, state.getWarning());
    }

    // Helper method
    private Scene createTestScene() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(new GameObject("obj-1", "TestObject", true, new ArrayList<>(), new Environment()));
        return new Scene("test-id", "Test Scene", objects, null);
    }
}