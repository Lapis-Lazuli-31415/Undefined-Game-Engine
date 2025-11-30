package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SpriteRenderer extends Property {

    private Image sprite;
    private boolean visible;
    private int opacity;
    private int zIndex;

    // Normal Constructor (used by your code)
    public SpriteRenderer(Image sprite, boolean visible) {
        this.sprite = sprite;
        this.visible = visible;
        this.opacity = 100;
        this.zIndex = 0;
    }

    // JSON Constructor (used by the Save/Load system)
    @JsonCreator
    public SpriteRenderer(
            @JsonProperty("sprite") Image sprite,
            @JsonProperty("visible") boolean visible,
            @JsonProperty("opacity") int opacity,
            @JsonProperty("z_index") int zIndex) { // 'z_index' matches the snake_case JSON
        this.sprite = sprite;
        this.visible = visible;
        this.opacity = opacity;
        this.zIndex = zIndex;
    }

    public Image getSprite() {
        return sprite;
    }

    public int getOpacity() {
        return opacity;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public void setVisible(boolean visiblity) {
        this.visible = visiblity;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    // Mark as ignored so we don't save duplicate "image" and "sprite" fields
    @JsonIgnore
    public Image getImage() {
        return sprite;
    }

    public int getZIndex() {
        return zIndex;
    }

    public boolean isVisible() {
        return visible;
    }

    @JsonIgnore
    public int getWidth() {
        return (this.sprite != null) ? this.sprite.getWidth() : 0;
    }

    @JsonIgnore
    public int getHeight() {
        return (this.sprite != null) ? this.sprite.getHeight() : 0;
    }
}