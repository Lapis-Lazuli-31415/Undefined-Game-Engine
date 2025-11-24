package use_case.validate_scene;

import entity.Scene;

/**
 * Input boundary for scene validation use case
 *
 * This interface defines the contract for validating a scene
 * before preview/testing or export.
 *
 * @author Wanru Cheng
 */
public interface ValidateSceneInputBoundary {

    /**
     * Validate a scene
     *
     * Checks:
     * - Scene has an ID (required)
     * - Scene has a name (required)
     * - Scene has at least one GameObject (required)
     * - Scene has background music (optional, warning if missing)
     *
     * @param scene The scene to validate
     * @return ValidationResult indicating success, error, or warning
     */
    ValidationResult validate(Scene scene);
}