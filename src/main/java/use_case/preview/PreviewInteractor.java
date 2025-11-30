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

        // Validate scene
        ValidationResult result = validator.validate(scene);

        // Extract simple types from Scene
        String sceneId = scene.getId() != null ? scene.getId().toString() : "unknown";
        String sceneName = scene.getName() != null ? scene.getName() : "Untitled";
        int gameObjectCount = scene.getGameObjects() != null ? scene.getGameObjects().size() : 0;

        if (result.isError()) {
            // Validation failed
            outputBoundary.presentError(result.getMessage());
        } else if (result.isWarning()) {
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