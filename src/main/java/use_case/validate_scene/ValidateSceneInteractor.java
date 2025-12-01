package use_case.validate_scene;

import entity.GameObject;
import entity.Scene;

import java.util.List;

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
        if (scene == null) {
            return ValidationResult.error("Scene is null");
        }

        if (scene.getId() == null) {
            return ValidationResult.error("Scene ID needed");
        }

        String name = scene.getName();
        if (name == null || name.trim().isEmpty()) {
            return ValidationResult.error("Scene Name needed");
        }

        List<GameObject> gameObjects = scene.getGameObjects();
        if (gameObjects == null || gameObjects.isEmpty()) {
            return ValidationResult.error("GameObjects needed");
        }

        return ValidationResult.valid();
    }
}