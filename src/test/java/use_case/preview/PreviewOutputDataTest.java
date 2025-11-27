package use_case.preview;

import entity.GameObject;
import entity.Property;
import entity.Scene;
import entity.scripting.environment.Environment;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for PreviewOutputData.
 *
 * @author Wanru Cheng
 */
class PreviewOutputDataTest {

    @Test
    void constructor_withSceneAndWarning_storesBoth() {
        // Arrange
        Scene scene = createTestScene();
        String warning = "Test warning message";

        // Act
        PreviewOutputData outputData = new PreviewOutputData(scene, warning);

        // Assert
        assertEquals(scene, outputData.getScene());
        assertEquals(warning, outputData.getWarning());
    }

    @Test
    void constructor_withSceneAndNullWarning_storesSceneAndNull() {
        // Arrange
        Scene scene = createTestScene();

        // Act
        PreviewOutputData outputData = new PreviewOutputData(scene, null);

        // Assert
        assertEquals(scene, outputData.getScene());
        assertNull(outputData.getWarning());
    }

    @Test
    void constructor_withNullSceneAndWarning_storesNullAndWarning() {
        // Arrange
        String warning = "Test warning";

        // Act
        PreviewOutputData outputData = new PreviewOutputData(null, warning);

        // Assert
        assertNull(outputData.getScene());
        assertEquals(warning, outputData.getWarning());
    }

    @Test
    void constructor_withBothNull_storesBothNull() {
        // Act
        PreviewOutputData outputData = new PreviewOutputData(null, null);

        // Assert
        assertNull(outputData.getScene());
        assertNull(outputData.getWarning());
    }

    @Test
    void getScene_returnsCorrectScene() {
        // Arrange
        Scene scene = createTestScene();
        PreviewOutputData outputData = new PreviewOutputData(scene, null);

        // Act & Assert
        assertSame(scene, outputData.getScene());
    }

    @Test
    void getWarning_returnsCorrectWarning() {
        // Arrange
        String warning = "This is a warning";
        PreviewOutputData outputData = new PreviewOutputData(null, warning);

        // Act & Assert
        assertEquals(warning, outputData.getWarning());
    }

    @Test
    void getWarning_withEmptyString_returnsEmptyString() {
        // Arrange
        PreviewOutputData outputData = new PreviewOutputData(null, "");

        // Act & Assert
        assertEquals("", outputData.getWarning());
    }

    // Helper method
    private Scene createTestScene() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(new GameObject("obj-1", "TestObject", true, new ArrayList<>(), new Environment()));
        return new Scene("test-id", "Test Scene", objects, null);
    }
}