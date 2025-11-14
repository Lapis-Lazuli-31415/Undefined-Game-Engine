package entity;

import java.nio.file.Path;
import java.util.UUID;

public abstract class Asset {

    private UUID id;
    private String name;
    private Path localpath;

    protected Asset(Path path) {
        this.localpath = path;
        this.id = UUID.randomUUID();
        this.name = path.getFileName().toString();
    }
}
