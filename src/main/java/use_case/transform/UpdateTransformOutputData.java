package use_case.transform;

/**
 * Output data for the UpdateTransform use case.
 *
 * This DTO is what crosses the boundary from the use case layer
 * into the interface adapter layer (Presenter).
 */
public class UpdateTransformOutputData {

    private final double x;
    private final double y;
    private final double scale;
    private final float rotation;

    public UpdateTransformOutputData(double x, double y, double scale, float rotation) {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.rotation = rotation;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getScale() {
        return scale;
    }

    public float getRotation() {
        return rotation;
    }
}
