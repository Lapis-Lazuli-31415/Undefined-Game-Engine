package entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Image extends Asset {
    private int width;
    private int height;
    private BufferedImage bimg;

    public Image(Path path) throws IOException {
        super(path);

        File file = path.toFile();
        this.bimg = ImageIO.read(file);

        if (this.bimg == null) {
            throw new IOException("Could not load image at: " + path);
        }

        this.height = bimg.getHeight();
        this.width = bimg.getWidth();
    }

    @ JsonIgnore // #TODO: IGNORE FOR NOW!! wait till misa does image
    public BufferedImage getBufferedImage() {
        return bimg;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
