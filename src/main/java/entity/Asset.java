package entity;

import java.nio.file.Path;
import java.util.UUID;

/**
 * An abstract class representing a generic asset.
 */

public abstract class Asset {

    private UUID id;
    private String name;
    Path localpath;     // note: not sure if i should keep path or switch to string/other datatype

    protected Asset(Path path) {
        this.id = UUID.randomUUID();
        this.localpath = path;
        this.name = path.getFileName().toString();
    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public Path getLocalpath() {
        return localpath;
    }
}
