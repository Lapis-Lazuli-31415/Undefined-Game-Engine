package interface_adapter;

/**
 * State object for the Import Sprite feature.
 * Contains all the data needed to display the result of sprite import.
 */
public class ImportSpriteState {

    private boolean success;
    private String message;
    private String spriteName;
    private String spriteId;

    public ImportSpriteState() {
        this.success = false;
        this.message = "";
        this.spriteName = "";
        this.spriteId = "";
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
}
