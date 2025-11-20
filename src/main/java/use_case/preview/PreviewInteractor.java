package use_case.preview;

import entity.Scene;
import use_case.validate_scene.ValidateSceneInputBoundary;
import use_case.validate_scene.ValidationResult;

/**
 * Interactor for preview use case.
 * Part of Use Case layer (pink ring in CA diagram).
 * Contains business logic for preview feature.
 *
 * @author Wanru Cheng
 */
public class PreviewInteractor implements PreviewInputBoundary {

    private final ValidateSceneInputBoundary validator;
    private final PreviewOutputBoundary outputBoundary;

    /**
     * Constructor.
     *
     * @param validator Scene validator
     * @param outputBoundary Output boundary (presenter)
     */
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

        if (result.isError()) {
            // Validation failed
            outputBoundary.presentError(result.getMessage());
        } else if (result.isWarning()) {
            // Validation passed with warning
            PreviewOutputData outputData = new PreviewOutputData(
                    scene,
                    result.getMessage()
            );
            outputBoundary.presentWarning(result.getMessage(), outputData);
        } else {
            // Validation passed
            PreviewOutputData outputData = new PreviewOutputData(scene, null);
            outputBoundary.presentSuccess(outputData);
        }
    }

    @Override
    public void stop() {
        // Stop preview logic
        // For now, just notify that preview stopped
    }
}