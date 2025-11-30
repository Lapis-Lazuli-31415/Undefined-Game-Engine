package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Vector;

/**
 * Transform represents the spatial info of a GameObject:
 * position (x, y), rotation, and scale (scaleX, scaleY).
 */
public class Transform {

    private final Vector<Double> position;   // position[0] = x, position[1] = y
    private float rotation;
    private final Vector<Double> scale;

    // Standard constructor
    public Transform(Vector<Double> position, float rotation, Vector<Double> scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    // JSON Constructor for Jackson
    // This takes the flat JSON fields (x, y, etc.) and rebuilds the Vectors.
    @JsonCreator
    public Transform(@JsonProperty("x") double x,
                     @JsonProperty("y") double y,
                     @JsonProperty("rotation") float rotation,
                     @JsonProperty("scale_x") double scaleX,
                     @JsonProperty("scale_y") double scaleY) {
        this.position = new Vector<>();
        this.position.add(x);
        this.position.add(y);

        this.rotation = rotation;

        this.scale = new Vector<>();
        this.scale.add(scaleX);
        this.scale.add(scaleY);
    }

    // --- Position access ---

    public double getX() {
        return position.get(0);
    }

    public double getY() {
        return position.get(1);
    }

    public void setX(double x) {
        position.set(0, x);
    }

    public void setY(double y) {
        position.set(1, y);
    }

    public void translate(double dx, double dy) {
        position.set(0, getX() + dx);
        position.set(1, getY() + dy);
    }

    // --- Scale access ---

    public double getScaleX() {
        return scale.get(0);
    }

    public double getScaleY() {
        return scale.get(1);
    }

    public void setScaleX(double sx) {
        scale.set(0, sx);
    }

    public void setScaleY(double sy) {
        scale.set(1, sy);
    }

    // --- Rotation ---

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}