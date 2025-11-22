package entity;

import java.nio.file.Path;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * An abstract class representing a generic asset.
 */
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
) // annotation used to store id of asset, which can be used to get assets in AssetLib
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
