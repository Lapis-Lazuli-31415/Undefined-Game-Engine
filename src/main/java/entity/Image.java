package entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Image extends Asset {
    private int width;
    private int height;
    BufferedImage bimg = ImageIO.read(new File(this.localpath.toString()));

    protected Image(Path path) throws IOException {
        super(path);
        this.height = bimg.getHeight();
        this.width = bimg.getWidth();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
