package entity;

import java.util.Vector;

public class Transform {

    final Vector<Double> position;
    final float rotation;
    final Vector<Double> scale;

    public Transform(Vector<Double> position, float rotation, Vector<Double> scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }
}
