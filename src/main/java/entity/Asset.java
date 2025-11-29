package entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.nio.file.Path;
import java.util.UUID;

/**
 * An abstract class representing a generic asset.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Image.class, name = "Image")
})
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