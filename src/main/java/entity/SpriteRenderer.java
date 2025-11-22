package entity;

import java.awt.*;

public class SpriteRenderer extends Property {

    private Image sprite;
    private boolean visible;
    private int opacity;

    public SpriteRenderer(Image sprite, boolean visible) {
        this.sprite = sprite;
        this.visible = visible;
        this.opacity = 100;
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
}
