package interface_adapter.transform;

import interface_adapter.ViewModel;

/**
 * View model for the Transform.
 * Wraps a TransformState and notifies observers on change.
 */
public class TransformViewModel extends ViewModel<TransformState> {

    public TransformViewModel() {
        super("transform");
        this.state = new TransformState();
    }

    public double getX() {
        return state.getX();
    }

    public void setX(double x) {
        state.setX(x);
        firePropertyChanged();
    }

    public double getY() {
        return state.getY();
    }

    public void setY(double y) {
        state.setY(y);
        firePropertyChanged();
    }

    public double getScale() {
        return state.getScale();
    }

    public void setScale(double scale) {
        state.setScale(scale);
        firePropertyChanged();
    }

    public float getRotation() {
        return state.getRotation();
    }

    public void setRotation(float rotation) {
        state.setRotation(rotation);
        firePropertyChanged();
    }
}
