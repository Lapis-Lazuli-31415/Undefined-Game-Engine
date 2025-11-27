package entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Image extends Asset {
    private final int width;
    private final int height;
    private final BufferedImage image;

    public Image(Path imagePath) throws IOException {
        super(imagePath);
        this.image = ImageIO.read(imagePath.toFile());
        this.width = image != null ? image.getWidth() : 0;
        this.height = image != null ? image.getHeight() : 0;
    }

    @JsonIgnore
    public BufferedImage getBufferedImage() {
        return image;  // Fix: bimg -> image
    }

    protected int getWidth() {
        return width;
    }

    protected int getHeight() {
        return height;
    }
}
