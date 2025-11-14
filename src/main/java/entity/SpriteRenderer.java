package entity;

import java.awt.*;

public class SpriteRenderer {

    final Image image;
    final boolean visible;

    public SpriteRenderer(Image image, boolean visible) {
        this.image = image;
        this.visible = visible;
    }
}
