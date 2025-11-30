package interface_adapter.preview;

/**
 * State for preview feature.
 * Holds all data needed by the View.
 *
 * Contains only SIMPLE TYPES (no Entity objects).
 *
 * @author Wanru Cheng
 */
public class PreviewState {

    private String sceneId;
    private String sceneName;
    private int gameObjectCount;
    private String error;
    private String warning;
    private boolean readyToPreview;

    /**
     * Constructor.
     */
    public PreviewState() {
        this.sceneId = null;
        this.sceneName = null;
        this.gameObjectCount = 0;
        this.error = null;
        this.warning = null;
        this.readyToPreview = false;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public int getGameObjectCount() {
        return gameObjectCount;
    }

    public void setGameObjectCount(int gameObjectCount) {
        this.gameObjectCount = gameObjectCount;
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