package entity;

import java.util.Vector;

/**
 * Transform represents the spatial info of a GameObject:
 * position (x, y), rotation, and scale (scaleX, scaleY).
 *
 * This is in the Entity layer and must not depend on JavaFX.
 */

public class Transform {

    private final Vector<Double> position;   // position[0] = x, position[1] = y
    private float rotation;
    private final Vector<Double> scale;

    public Transform(Vector<Double> position, float rotation, Vector<Double> scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
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
    // --- Copy method for preview isolation ---

    /**
     * Create a deep copy of this Transform.
     *
     * @return A new Transform with copied position, rotation, and scale
     */
    public Transform copy() {
        Vector<Double> copiedPosition = new Vector<>();
        copiedPosition.add(this.position.get(0));
        copiedPosition.add(this.position.get(1));

        Vector<Double> copiedScale = new Vector<>();
        copiedScale.add(this.scale.get(0));
        copiedScale.add(this.scale.get(1));

        return new Transform(copiedPosition, this.rotation, copiedScale);
    }
}
