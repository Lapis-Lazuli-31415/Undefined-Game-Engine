package use_case.preview;

import entity.Scene;

/**
 * Input data for preview use case.
 * Data transfer object.
 *
 * @author Wanru Cheng
 */
public class PreviewInputData {

    private final Scene scene;

    /**
     * Constructor.
     *
     * @param scene The scene to preview
     */
    public PreviewInputData(Scene scene) {
        this.scene = scene;
    }

    /**
     * Get scene.
     *
     * @return The scene
     */
    public Scene getScene() {
        return scene;
    }
}