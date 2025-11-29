package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URI;

public class Image extends Asset {
    private final int width;
    private final int height;
    private final BufferedImage image;

    // Normal constructor
    public Image(Path imagePath) throws IOException {
        super(imagePath);
        this.image = ImageIO.read(imagePath.toFile());
        this.width = image != null ? image.getWidth() : 0;
        this.height = image != null ? image.getHeight() : 0;
    }

    // JSON Constructor (REQUIRED for loading)
    @JsonCreator
    public Image(@JsonProperty("localpath") String localpath) throws IOException {
        super(Paths.get(URI.create(localpath)));
        this.image = ImageIO.read(this.getLocalpath().toFile());
        this.width = image != null ? image.getWidth() : 0;
        this.height = image != null ? image.getHeight() : 0;
    }

    protected int getWidth() { return width; }
    protected int getHeight() { return height; }

    @JsonIgnore
    public BufferedImage getBufferedImage() { return image; }
}