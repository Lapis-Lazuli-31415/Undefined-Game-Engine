package entity;

import java.nio.file.Path;

public class Image extends Asset {
    private int width;
    private int height;

    protected Image(Path path) {
        super(path);
    }
}
