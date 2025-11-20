package use_case.transform;

import entity.GameObject;
import entity.Transform;

/**
 * Use case: update the Transform of a GameObject.
 * This sits in the use_case layer and only depends on entities and boundaries.
 */
public class UpdateTransformInteractor implements UpdateTransformInputBoundary {

    private final GameObject gameObject;
    private final UpdateTransformOutputBoundary presenter;

    public UpdateTransformInteractor(GameObject gameObject,
                                     UpdateTransformOutputBoundary presenter) {
        this.gameObject = gameObject;
        this.presenter = presenter;
    }

    @Override
    public void updateTransform(UpdateTransformInputData data) {
        Transform t = gameObject.getTransform();
        if (t == null) {
            // If somehow null, you could create one here or just return.
            return;
        }

        t.setX(data.x);
        t.setY(data.y);
        t.setScaleX(data.scale);
        t.setScaleY(data.scale);
        t.setRotation(data.rotation);

        presenter.presentTransform(t);
    }
}
