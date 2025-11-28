package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SpriteRenderer extends Property {

    private Image sprite;
    private boolean visible;
    private int opacity;
    private int zIndex;

    public SpriteRenderer(Image sprite, boolean visible) {
        this.sprite = sprite;
        this.visible = visible;
        this.opacity = 100;
    }

    @JsonIgnore // #TODO: IGNORE FOR NOW!! wait till misa does image
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

    // for Jackson saving part
    public Image getImage() {
        return sprite;
    }

    public int getZIndex() {
        return zIndex;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getWidth() {
        return this.sprite.getWidth();
    }

    public int getHeight() {
        return this.sprite.getHeight();
    }
    // --- Copy method for preview isolation ---

    /**
     * Create a deep copy of this SpriteRenderer.
     * Note: The Image (sprite) is shared as it's immutable asset data.
     *
     * @return A new SpriteRenderer with copied state
     */
    public SpriteRenderer copy() {
        SpriteRenderer copy = new SpriteRenderer(this.sprite, this.visible);
        copy.setOpacity(this.opacity);
        copy.zIndex = this.zIndex;
        return copy;
    }
}
