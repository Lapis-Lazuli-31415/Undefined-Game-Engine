package use_case.validate_scene;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ValidationResult
 *
 * @author Wanru Cheng
 */
class ValidationResultTest {

    @Test
    void testValidResult() {
        ValidationResult result = ValidationResult.valid();

        assertTrue(result.isValid());
        assertFalse(result.isError());
        assertFalse(result.isWarning());
        assertNull(result.getMessage());
        assertEquals(ValidationResult.Type.VALID, result.getType());
    }

    @Test
    void testErrorResult() {
        String errorMessage = "Scene ID needed";
        ValidationResult result = ValidationResult.error(errorMessage);

        assertFalse(result.isValid());
        assertTrue(result.isError());
        assertFalse(result.isWarning());
        assertEquals(errorMessage, result.getMessage());
        assertEquals(ValidationResult.Type.ERROR, result.getType());
    }

    @Test
    void testWarningResult() {
        String warningMessage = "No background music found";
        ValidationResult result = ValidationResult.warning(warningMessage);

        assertTrue(result.isValid());  // Warning is still valid
        assertFalse(result.isError());
        assertTrue(result.isWarning());
        assertEquals(warningMessage, result.getMessage());
        assertEquals(ValidationResult.Type.WARNING, result.getType());
    }

    @Test
    void testToString() {
        ValidationResult result = ValidationResult.error("Test error");
        String str = result.toString();

        assertTrue(str.contains("ERROR"));
        assertTrue(str.contains("Test error"));
    }
}