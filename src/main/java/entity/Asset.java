package entity;

import java.nio.file.Path;
import java.util.UUID;

/**
 * An abstract class representing a generic asset.
 */

public abstract class Asset {

    private UUID id;
    private String name;
    private Path localpath;     // note: not sure if i should keep path or switch to string/other datatype

    protected Asset(Path path) {
        this.localpath = path;
        this.id = UUID.randomUUID();
        this.name = path.getFileName().toString();
    }
}
