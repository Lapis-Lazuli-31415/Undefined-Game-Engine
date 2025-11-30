package interface_adapter.preview;

import entity.Scene;
import use_case.preview.PreviewInputBoundary;
import use_case.preview.PreviewInputData;

/**
 * Controller for preview feature.
 * Part of Interface Adapter layer (green ring in CA diagram).
 * Receives user input from View and converts to Use Case input.
 *
 * @author Wanru Cheng
 */
public class PreviewController {

    private final PreviewInputBoundary previewInteractor;

    /**
     * Constructor.
     *
     * @param previewInteractor The use case interactor
     */
    public PreviewController(PreviewInputBoundary previewInteractor) {
        this.previewInteractor = previewInteractor;
    }

    /**
     * Execute preview use case.
     * Called when user clicks Play button.
     *
     * @param scene The scene to preview
     */
    public void execute(Scene scene) {
        PreviewInputData inputData = new PreviewInputData(scene);
        previewInteractor.execute(inputData);
    }

    /**
     * Stop preview.
     * Called when user clicks Stop button.
     */
    public void stop() {
        previewInteractor.stop();
    }
}