package use_case.preview;

import entity.Scene;
import use_case.validate_scene.ValidateSceneInputBoundary;
import use_case.validate_scene.ValidationResult;

/**
 * Interactor for preview use case.
 * Part of Use Case layer (pink ring in CA diagram).
 * Contains business logic for preview feature.
 *
 * Extracts simple types from Entity and passes to Presenter.
 *
 * @author Wanru Cheng
 */
public class PreviewInteractor implements PreviewInputBoundary {

    private final ValidateSceneInputBoundary validator;
    private final PreviewOutputBoundary outputBoundary;

    public PreviewInteractor(
            ValidateSceneInputBoundary validator,
            PreviewOutputBoundary outputBoundary) {
        this.validator = validator;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(PreviewInputData inputData) {
        Scene scene = inputData.getScene();

        // ✅ Step 1: Validate scene FIRST
        ValidationResult result = validator.validate(scene);

        // ✅ Step 2: Check for errors BEFORE accessing scene
        if (result.isError()) {
            // Validation failed - DON'T access scene properties!
            outputBoundary.presentError(result.getMessage());
            return;  // Exit early
        }

        // ✅ Step 3: NOW it's safe to extract scene properties
        // (We know scene is not null and has valid ID, name, objects)
        String sceneId = scene.getId() != null ? scene.getId().toString() : "unknown";
        String sceneName = scene.getName() != null ? scene.getName() : "Untitled";
        int gameObjectCount = scene.getGameObjects() != null ? scene.getGameObjects().size() : 0;

        // ✅ Step 4: Handle warning or success
        if (result.isWarning()) {
            // Validation passed with warning
            PreviewOutputData outputData = new PreviewOutputData(
                    sceneId,
                    sceneName,
                    gameObjectCount,
                    result.getMessage(),
                    true
            );
            outputBoundary.presentWarning(result.getMessage(), outputData);
        } else {
            // Validation passed
            PreviewOutputData outputData = new PreviewOutputData(
                    sceneId,
                    sceneName,
                    gameObjectCount,
                    null,
                    true
            );
            outputBoundary.presentSuccess(outputData);
        }
    }

    @Override
    public void stop() {
        // Stop preview logic
    }
}