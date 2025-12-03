package use_case.validate_scene;

import entity.GameObject;
import entity.Scene;
import entity.Transform;
import entity.scripting.TriggerManager;
import entity.scripting.environment.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.Vector;

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
        Scene scene = new Scene(null, "Test Scene", objects);

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
        Scene scene = new Scene(UUID.randomUUID(), null, objects);

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
        Scene scene = new Scene(UUID.randomUUID(), "", objects);

        // Act
        ValidationResult result = validator.validate(scene);

        // Assert
        assertTrue(result.isError());
        assertEquals("Scene Name needed", result.getMessage());
    }

    @Test
    void validate_sceneWithNullGameObjects_returnsError() {
        // Arrange
        Scene scene = new Scene(UUID.randomUUID(), "Test Scene", null);

        // Act
        ValidationResult result = validator.validate(scene);

        // Assert
        assertTrue(result.isError());
        assertEquals("GameObjects needed", result.getMessage());
    }

    @Test
    void validate_sceneWithEmptyGameObjects_returnsError() {
        // Arrange
        Scene scene = new Scene(UUID.randomUUID(), "Test Scene", new ArrayList<>());

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
        objects.add(new GameObject("obj-2", "Object2", true, new Environment(), createDefaultTransform(), null, new TriggerManager()));
        objects.add(new GameObject("obj-3", "Object3", true, new Environment(), createDefaultTransform(), null, new TriggerManager()));
        Scene scene = new Scene(UUID.randomUUID(), "Test Scene", objects);

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
        return new Scene(UUID.randomUUID(), "Test Scene", objects);
    }

    private GameObject createTestGameObject() {
        return new GameObject(
                "obj-1",
                "TestObject",
                true,
                new Environment(),
                createDefaultTransform(),
                null,  // spriteRenderer
                new TriggerManager()
        );
    }

    private Transform createDefaultTransform() {
        Vector<Double> position = new Vector<>(Arrays.asList(0.0, 0.0));
        Vector<Double> scale = new Vector<>(Arrays.asList(1.0, 1.0));
        return new Transform(position, 0f, scale);
    }
}