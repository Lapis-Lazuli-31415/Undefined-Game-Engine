package entity;

//import java.awt.*;    I dont think we need this import in JavaFX
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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
