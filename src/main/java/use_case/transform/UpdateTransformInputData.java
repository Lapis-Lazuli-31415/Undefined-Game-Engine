package use_case.transform;

public class UpdateTransformInputData {
    public final double x;
    public final double y;
    public final double scale;
    public final float rotation;

    public UpdateTransformInputData(double x, double y, double scale, float rotation) {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.rotation = rotation;
    }
}
