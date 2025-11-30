package entity;

import com.fasterxml.jackson.annotation.*;
import entity.scripting.expression.SimpleArithmeticOperation;
import entity.scripting.expression.value.NumericValue;
import entity.scripting.expression.variable.NumericVariable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URI;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Image.class, name = "Image")
})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    // JSON Constructor (REQUIRED for loading)
    @JsonCreator
    public Image(@JsonProperty("localpath") String localpath) throws IOException {
        super(Paths.get(URI.create(localpath)));
        this.image = ImageIO.read(this.getLocalpath().toFile());
        this.width = image != null ? image.getWidth() : 0;
        this.height = image != null ? image.getHeight() : 0;
    }

    protected int getWidth() { return width; }
    protected int getHeight() { return height; }

    @JsonIgnore
    public BufferedImage getBufferedImage() { return image; }
}