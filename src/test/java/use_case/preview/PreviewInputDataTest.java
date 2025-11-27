package use_case.preview;

import entity.GameObject;
import entity.Property;
import entity.Scene;
import entity.scripting.environment.Environment;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for PreviewInputData.
 *
 * @author Wanru Cheng
 */
class PreviewInputDataTest {

    @Test
    void constructor_withScene_storesScene() {
        // Arrange
        Scene scene = createTestScene();

        // Act
        PreviewInputData inputData = new PreviewInputData(scene);

        // Assert
        assertEquals(scene, inputData.getScene());
    }

    @Test
    void constructor_withNullScene_storesNull() {
        // Act
        PreviewInputData inputData = new PreviewInputData(null);

        // Assert
        assertNull(inputData.getScene());
    }

    @Test
    void getScene_returnsSameInstance() {
        // Arrange
        Scene scene = createTestScene();
        PreviewInputData inputData = new PreviewInputData(scene);

        // Act & Assert
        assertSame(scene, inputData.getScene());
    }

    // Helper method
    private Scene createTestScene() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(new GameObject("obj-1", "TestObject", true, new ArrayList<>(), new Environment()));
        return new Scene("test-id", "Test Scene", objects, null);
    }
}