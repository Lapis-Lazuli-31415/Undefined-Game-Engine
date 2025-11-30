package use_case.preview;

/**
 * Output data for preview use case.
 * Data transfer object with SIMPLE TYPES ONLY.
 *
 * Follows Clean Architecture: Output Data should NOT contain Entity objects.
 * Contains only primitive types and Strings for Presenter to format.
 *
 * @author Wanru Cheng
 */
public class PreviewOutputData {

    private final String sceneId;
    private final String sceneName;
    private final int gameObjectCount;
    private final String warning;
    private final boolean isValid;

    /**
     * Constructor.
     *
     * @param sceneId The scene ID
     * @param sceneName The scene name
     * @param gameObjectCount Number of game objects
     * @param warning Warning message (null if none)
     * @param isValid Whether scene is valid
     */
    public PreviewOutputData(String sceneId, String sceneName,
                             int gameObjectCount, String warning, boolean isValid) {
        this.sceneId = sceneId;
        this.sceneName = sceneName;
        this.gameObjectCount = gameObjectCount;
        this.warning = warning;
        this.isValid = isValid;
    }

    public String getSceneId() {
        return sceneId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public int getGameObjectCount() {
        return gameObjectCount;
    }

    public String getWarning() {
        return warning;
    }

    public boolean isValid() {
        return isValid;
    }
}