package use_case.preview;

import entity.Scene;

/**
 * Output data for preview use case.
 * Data transfer object.
 *
 * @author Wanru Cheng
 */
public class PreviewOutputData {

    private final Scene scene;
    private final String warning;

    /**
     * Constructor.
     *
     * @param scene The validated scene
     * @param warning Warning message (null if none)
     */
    public PreviewOutputData(Scene scene, String warning) {
        this.scene = scene;
        this.warning = warning;
    }

    /**
     * Get scene.
     *
     * @return The scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Get warning.
     *
     * @return Warning message or null
     */
    public String getWarning() {
        return warning;
    }
}