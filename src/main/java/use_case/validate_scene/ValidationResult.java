package use_case.validate_scene;

/**
 * Validation result for scene validation
 *
 * @author Wanru Cheng
 */
public class ValidationResult {

    public enum Type {
        VALID,      // Scene is valid, no issues
        ERROR,      // Scene has critical errors, cannot proceed
        WARNING     // Scene has warnings, user can choose to proceed
    }

    private final Type type;
    private final String message;

    private ValidationResult(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    /**
     * Create a valid result (no errors or warnings)
     */
    public static ValidationResult valid() {
        return new ValidationResult(Type.VALID, null);
    }

    /**
     * Create an error result (critical error, cannot proceed)
     */
    public static ValidationResult error(String message) {
        return new ValidationResult(Type.ERROR, message);
    }

    /**
     * Create a warning result (can proceed with user confirmation)
     */
    public static ValidationResult warning(String message) {
        return new ValidationResult(Type.WARNING, message);
    }

    /**
     * Check if validation passed (valid or warning)
     */
    public boolean isValid() {
        return type == Type.VALID || type == Type.WARNING;
    }

    /**
     * Check if validation failed with error
     */
    public boolean isError() {
        return type == Type.ERROR;
    }

    /**
     * Check if validation has warning
     */
    public boolean isWarning() {
        return type == Type.WARNING;
    }

    /**
     * Get the validation message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the validation type
     */
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ValidationResult{type=" + type + ", message='" + message + "'}";
    }
}