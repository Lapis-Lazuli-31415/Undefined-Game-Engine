package entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.nio.file.Path;
import java.util.UUID;

/**
 * An abstract class representing a generic asset.
 * Hard-coded to always deserialize as an Image.
 */
@JsonDeserialize(as = Image.class)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public abstract class Asset {

    private UUID id;
    private String name;
    Path localpath;

    protected Asset() {
        this.id = UUID.randomUUID();
    }

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