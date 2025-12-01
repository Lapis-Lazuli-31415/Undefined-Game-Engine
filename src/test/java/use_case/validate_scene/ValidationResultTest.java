package use_case.validate_scene;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ValidationResult.
 *
 * @author Wanru Cheng
 */
class ValidationResultTest {

    @Test
    void valid_createsValidResult() {
        // Act
        ValidationResult result = ValidationResult.valid();

        // Assert
        assertTrue(result.isValid());
        assertFalse(result.isError());
        assertFalse(result.isWarning());
        assertEquals(ValidationResult.Type.VALID, result.getType());
        assertNull(result.getMessage());
    }

    @Test
    void error_createsErrorResult() {
        // Arrange
        String errorMessage = "Test error message";

        // Act
        ValidationResult result = ValidationResult.error(errorMessage);

        // Assert
        assertTrue(result.isError());
        assertFalse(result.isValid());
        assertFalse(result.isWarning());
        assertEquals(ValidationResult.Type.ERROR, result.getType());
        assertEquals(errorMessage, result.getMessage());
    }

    @Test
    void warning_createsWarningResult() {
        // Arrange
        String warningMessage = "Test warning message";

        // Act
        ValidationResult result = ValidationResult.warning(warningMessage);

        // Assert
        assertTrue(result.isWarning());
        assertTrue(result.isValid());  // Warning is still valid
        assertFalse(result.isError());
        assertEquals(ValidationResult.Type.WARNING, result.getType());
        assertEquals(warningMessage, result.getMessage());
    }

    @Test
    void toString_containsTypeAndMessage() {
        // Arrange
        ValidationResult result = ValidationResult.error("Test error");

        // Act
        String str = result.toString();

        // Assert
        assertTrue(str.contains("ERROR"));
        assertTrue(str.contains("Test error"));
    }

    @Test
    void getType_returnsCorrectType() {
        // Assert
        assertEquals(ValidationResult.Type.VALID, ValidationResult.valid().getType());
        assertEquals(ValidationResult.Type.ERROR, ValidationResult.error("error").getType());
        assertEquals(ValidationResult.Type.WARNING, ValidationResult.warning("warning").getType());
    }
}