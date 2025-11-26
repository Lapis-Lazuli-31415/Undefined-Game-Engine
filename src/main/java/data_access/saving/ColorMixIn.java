package data_access.saving;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.awt.color.ColorSpace;

// class to ignore useless colour properties in the Color class from java library
public abstract class ColorMixIn {
    // ignore the complex ColorSpace object
    @JsonIgnore
    abstract ColorSpace getColorSpace();

    // ignore the transparency integer
    @JsonIgnore
    abstract int getTransparency();

    // ignore the packed integer RGB value
    @JsonIgnore
    abstract int getRGB();

    // ignore alpha
    @JsonIgnore
    abstract int getAlpha();
}