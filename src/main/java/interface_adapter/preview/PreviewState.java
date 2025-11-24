package interface_adapter.preview;

import entity.Scene;

/**
 * State for preview feature.
 * Holds all data needed by the View.
 *
 * @author Wanru Cheng
 */
public class PreviewState {

    private Scene scene;
    private String error;
    private String warning;
    private boolean readyToPreview;

    /**
     * Constructor.
     */
    public PreviewState() {
        this.scene = null;
        this.error = null;
        this.warning = null;
        this.readyToPreview = false;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public boolean isReadyToPreview() {
        return readyToPreview;
    }

    public void setReadyToPreview(boolean readyToPreview) {
        this.readyToPreview = readyToPreview;
    }
}