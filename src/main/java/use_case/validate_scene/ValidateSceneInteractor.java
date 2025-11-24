package use_case.validate_scene;

import entity.Scene;

/**
 * Interactor for validating a scene before preview/testing
 *
 * Business rules:
 * 1. Scene must have an ID (required)
 * 2. Scene must have a name (required)
 * 3. Scene must have at least one GameObject (required)
 * 4. Background music is optional (warning if missing)
 *
 * @author Wanru Cheng
 */
public class ValidateSceneInteractor implements ValidateSceneInputBoundary {

    @Override
    public ValidationResult validate(Scene scene) {
        // Check if scene is null
        if (scene == null) {
            return ValidationResult.error("Scene is null");
        }

        // Rule 1: Check ID (required)
        if (scene.getId() == null || scene.getId().trim().isEmpty()) {
            return ValidationResult.error("Scene ID needed");
        }

        // Rule 2: Check Name (required)
        if (scene.getName() == null || scene.getName().trim().isEmpty()) {
            return ValidationResult.error("Scene Name needed");
        }

        // Rule 3: Check GameObjects (required)
        if (scene.getGameObjects() == null || scene.getGameObjects().isEmpty()) {
            return ValidationResult.error("GameObjects needed");
        }

        // Rule 4: Check Background Music (optional - warning only)
        if (scene.getBackgroundMusic() == null) {
            return ValidationResult.warning(
                    "No background music found. Continue without music?"
            );
        }

        // All checks passed
        return ValidationResult.valid();
    }
}