package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
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

    // JSON Constructor
    @JsonCreator
    public Image(@JsonProperty("localpath") String localpath) {
        // 1. Use helper to handle "file:" URIs vs simple paths
        super(parsePath(localpath));

        // 2. Try to load the image data safely
        BufferedImage tempImage = null;
        try {
            File file = this.getLocalpath().toFile();
            if (file.exists()) {
                tempImage = ImageIO.read(file);
            } else {
                System.err.println("⚠️ Warning: Image file missing at: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("⚠️ Warning: Failed to read image file: " + e.getMessage());
        }

        this.image = tempImage;
        this.width = (image != null) ? image.getWidth() : 0;
        this.height = (image != null) ? image.getHeight() : 0;
    }

    // Helper to handle paths that might be encoded as URIs (e.g. "file:/Users/...")
    private static Path parsePath(String pathStr) {
        if (pathStr != null && pathStr.startsWith("file:")) {
            try {
                return Paths.get(URI.create(pathStr));
            } catch (Exception e) {
                // If URI parsing fails, fall back to simple string path
                System.err.println("⚠️ Could not parse URI: " + pathStr + " -> " + e.getMessage());
            }
        }
        return Paths.get(pathStr);
    }

    protected int getWidth() { return width; }
    protected int getHeight() { return height; }

    @JsonIgnore
    public BufferedImage getBufferedImage() { return image; }
}