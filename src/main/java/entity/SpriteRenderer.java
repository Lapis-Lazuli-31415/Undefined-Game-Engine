package entity;

import java.awt.*;

public class SpriteRenderer {

    final Image image;
    final Color tint;
    final int zIndex;
    final boolean visible;

    public SpriteRenderer(Image image, Color tint, int zIndex, boolean visible) {
        this.image = image;
        this.tint = tint;
        this.zIndex = zIndex;
        this.visible = visible;
    }
}
