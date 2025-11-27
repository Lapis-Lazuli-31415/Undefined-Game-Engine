package use_case.validate_scene;

import entity.GameObject;
import entity.Property;
import entity.Scene;
import entity.scripting.environment.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ValidateSceneInteractor.
 *
 * @author Wanru Cheng
 */
class ValidateSceneInteractorTest {

    private ValidateSceneInteractor validator;

    @BeforeEach
    void setUp() {
        validator = new ValidateSceneInteractor();
    }

    @Test
    void validate_validScene_returnsValid() {
        // Arrange
        Scene scene = createValidScene();

        // Act
        ValidationResult result = validator.validate(scene);

        // Assert
        assertTrue(result.isValid());
        assertFalse(result.isError());
        assertEquals(ValidationResult.Type.VALID, result.getType());
    }

    @Test
    void validate_nullScene_returnsError() {
        // Act
        ValidationResult result = validator.validate(null);

        // Assert
        assertTrue(result.isError());
        assertFalse(result.isValid());
        assertEquals("Scene is null", result.getMessage());
    }

    @Test
    void validate_sceneWithNullId_returnsError() {
        // Arrange
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        Scene scene = new Scene(null, "Test Scene", objects, null);

        // Act
        ValidationResult result = validator.validate(scene);

        // Assert
        assertTrue(result.isError());
        assertEquals("Scene ID needed", result.getMessage());
    }

    @Test
    void validate_sceneWithEmptyId_returnsError() {
        // Arrange
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        Scene scene = new Scene("", "Test Scene", objects, null);

        // Act
        ValidationResult result = validator.validate(scene);

        // Assert
        assertTrue(result.isError());
        assertEquals("Scene ID needed", result.getMessage());
    }

    @Test
    void validate_sceneWithWhitespaceId_returnsError() {
        // Arrange
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        Scene scene = new Scene("   ", "Test Scene", objects, null);

        // Act
        ValidationResult result = validator.validate(scene);

        // Assert
        assertTrue(result.isError());
        assertEquals("Scene ID needed", result.getMessage());
    }

    @Test
    void validate_sceneWithNullName_returnsError() {
        // Arrange
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        Scene scene = new Scene("test-id", null, objects, null);

        // Act
        ValidationResult result = validator.validate(scene);

        // Assert
        assertTrue(result.isError());
        assertEquals("Scene Name needed", result.getMessage());
    }

    @Test
    void validate_sceneWithEmptyName_returnsError() {
        // Arrange
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        Scene scene = new Scene("test-id", "", objects, null);

        // Act
        ValidationResult result = validator.validate(scene);

        // Assert
        assertTrue(result.isError());
        assertEquals("Scene Name needed", result.getMessage());
    }

    @Test
    void validate_sceneWithNullGameObjects_returnsError() {
        // Arrange
        Scene scene = new Scene("test-id", "Test Scene", null, null);

        // Act
        ValidationResult result = validator.validate(scene);

        // Assert
        assertTrue(result.isError());
        assertEquals("GameObjects needed", result.getMessage());
    }

    @Test
    void validate_sceneWithEmptyGameObjects_returnsError() {
        // Arrange
        Scene scene = new Scene("test-id", "Test Scene", new ArrayList<>(), null);

        // Act
        ValidationResult result = validator.validate(scene);

        // Assert
        assertTrue(result.isError());
        assertEquals("GameObjects needed", result.getMessage());
    }

    @Test
    void validate_sceneWithMultipleGameObjects_returnsValid() {
        // Arrange
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        objects.add(new GameObject("obj-2", "Object2", true, new ArrayList<>(), null));
        objects.add(new GameObject("obj-3", "Object3", true, new ArrayList<>(), null));
        Scene scene = new Scene("test-id", "Test Scene", objects, null);

        // Act
        ValidationResult result = validator.validate(scene);

        // Assert
        assertTrue(result.isValid());
        assertFalse(result.isError());
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
}