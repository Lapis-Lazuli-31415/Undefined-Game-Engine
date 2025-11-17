package entity;

import java.awt.*;

public class SpriteRenderer extends Property{

    public final Image image;
    public final Color tint;
    public final int zIndex;
    public final boolean visible;

    public SpriteRenderer(Image image, Color tint, int zIndex, boolean visible) {
        this.image = image;
        this.tint = tint;
        this.zIndex = zIndex;
        this.visible = visible;
    }
}
