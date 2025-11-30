package interface_adapter.sprites;

/**
 * State object for the Import Sprite from Unsplash view model.
 * Contains all the data needed to display the result of Unsplash sprite import.
 */
public class ImportSpriteFromUnsplashState {

    private boolean success;
    private String message;
    private String spriteName;
    private String spriteId;
    private String searchQuery;
    private String selectedImageId;
    private String targetFileName;
    private boolean isLoading;

    public ImportSpriteFromUnsplashState() {
        this.success = false;
        this.message = "";
        this.spriteName = "";
        this.spriteId = "";
        this.searchQuery = "";
        this.selectedImageId = "";
        this.targetFileName = "";
        this.isLoading = false;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSpriteName() {
        return spriteName;
    }

    public void setSpriteName(String spriteName) {
        this.spriteName = spriteName;
    }

    public String getSpriteId() {
        return spriteId;
    }

    public void setSpriteId(String spriteId) {
        this.spriteId = spriteId;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSelectedImageId() {
        return selectedImageId;
    }

    public void setSelectedImageId(String selectedImageId) {
        this.selectedImageId = selectedImageId;
    }

    public String getTargetFileName() {
        return targetFileName;
    }

    public void setTargetFileName(String targetFileName) {
        this.targetFileName = targetFileName;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}

