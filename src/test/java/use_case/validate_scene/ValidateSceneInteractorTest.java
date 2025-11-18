package use_case.validate_scene;

import entity.Scene;
import entity.GameObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ValidateSceneInteractor
 *
 * Tests all validation rules:
 * 1. Scene must not be null
 * 2. Scene must have an ID
 * 3. Scene must have a name
 * 4. Scene must have at least one GameObject
 * 5. Background music is optional (warning if missing)
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
    void testValidateNullScene() {
        ValidationResult result = validator.validate(null);

        assertTrue(result.isError());
        assertEquals("Scene is null", result.getMessage());
    }

    @Test
    void testValidateSceneWithoutId() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());

        Scene scene = new Scene(null, "Test Scene", objects, null);
        ValidationResult result = validator.validate(scene);

        assertTrue(result.isError());
        assertEquals("Scene ID needed", result.getMessage());
    }

    @Test
    void testValidateSceneWithEmptyId() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());

        Scene scene = new Scene("", "Test Scene", objects, null);
        ValidationResult result = validator.validate(scene);

        assertTrue(result.isError());
        assertEquals("Scene ID needed", result.getMessage());
    }

    @Test
    void testValidateSceneWithWhitespaceId() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());

        Scene scene = new Scene("   ", "Test Scene", objects, null);
        ValidationResult result = validator.validate(scene);

        assertTrue(result.isError());
        assertEquals("Scene ID needed", result.getMessage());
    }

    @Test
    void testValidateSceneWithoutName() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());

        Scene scene = new Scene("test-id", null, objects, null);
        ValidationResult result = validator.validate(scene);

        assertTrue(result.isError());
        assertEquals("Scene Name needed", result.getMessage());
    }

    @Test
    void testValidateSceneWithEmptyName() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());

        Scene scene = new Scene("test-id", "", objects, null);
        ValidationResult result = validator.validate(scene);

        assertTrue(result.isError());
        assertEquals("Scene Name needed", result.getMessage());
    }

    @Test
    void testValidateSceneWithWhitespaceName() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());

        Scene scene = new Scene("test-id", "   ", objects, null);
        ValidationResult result = validator.validate(scene);

        assertTrue(result.isError());
        assertEquals("Scene Name needed", result.getMessage());
    }

    @Test
    void testValidateSceneWithEmptyGameObjects() {
        ArrayList<GameObject> objects = new ArrayList<>();
        Scene scene = new Scene("test-id", "Test Scene", objects, null);
        ValidationResult result = validator.validate(scene);

        assertTrue(result.isError());
        assertEquals("GameObjects needed", result.getMessage());
    }

    @Test
    void testValidateSceneWithoutMusic_ReturnsWarning() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());

        // Scene without music should return warning
        Scene scene = new Scene("test-id", "Test Scene", objects, null);
        ValidationResult result = validator.validate(scene);

        // Warning is still considered valid (user can proceed)
        assertTrue(result.isValid());
        assertTrue(result.isWarning());
        assertFalse(result.isError());
        assertNotNull(result.getMessage());
        assertTrue(result.getMessage().toLowerCase().contains("music"));
    }

    @Test
    void testValidateSceneWithSingleGameObject() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());

        Scene scene = new Scene("test-id", "Test Scene", objects, null);
        ValidationResult result = validator.validate(scene);

        // Should be valid (only warning for music)
        assertTrue(result.isValid());
        assertTrue(result.isWarning());
    }

    @Test
    void testValidateSceneWithMultipleGameObjects() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        objects.add(createTestGameObject());
        objects.add(createTestGameObject());

        // Even without music, should only be a warning
        Scene scene = new Scene("test-id", "Test Scene", objects, null);
        ValidationResult result = validator.validate(scene);

        assertTrue(result.isValid());
        assertTrue(result.isWarning());
    }

    @Test
    void testValidateSceneWithAllRequiredFields() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());

        // Test with all required fields (ID, name, GameObjects)
        // Music is optional, so null music should only warn, not error
        Scene scene = new Scene("valid-id", "Valid Scene", objects, null);
        ValidationResult result = validator.validate(scene);

        // Should be valid (warning for music is okay)
        assertTrue(result.isValid());
    }

    @Test
    void testValidateSceneWithLongId() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());

        String longId = "very-long-scene-id-with-many-characters-" +
                "to-test-that-long-ids-work-correctly";
        Scene scene = new Scene(longId, "Test Scene", objects, null);
        ValidationResult result = validator.validate(scene);

        assertTrue(result.isValid());
    }

    @Test
    void testValidateSceneWithSpecialCharactersInId() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());

        Scene scene = new Scene("test-id_123", "Test Scene", objects, null);
        ValidationResult result = validator.validate(scene);

        assertTrue(result.isValid());
    }

    @Test
    void testValidateSceneWithSpecialCharactersInName() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());

        Scene scene = new Scene("test-id", "Test Scene - Level 1!", objects, null);
        ValidationResult result = validator.validate(scene);

        assertTrue(result.isValid());
    }

    /**
     * Helper method to create a test GameObject
     */
    private GameObject createTestGameObject() {
        return new GameObject(
                "test-obj-" + System.nanoTime(),  // Unique ID
                "Test Object",
                true,
                new ArrayList<>(),
                null
        );
    }
}