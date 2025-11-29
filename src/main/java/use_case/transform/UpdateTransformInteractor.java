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
        if (gameObject == null) {
            // In a more advanced version you could add a failure method to the boundary.
            return;
        }

        Transform t = gameObject.getTransform();
        if (t == null) {
            // If somehow null, you could create one here or just return.
            return;
        }

        // Mutate the entity (domain logic)
        t.setX(data.x);
        t.setY(data.y);
        t.setScaleX(data.scale);
        t.setScaleY(data.scale);
        t.setRotation(data.rotation);

        // Build output data for the Presenter instead of passing the entity through.
        UpdateTransformOutputData outputData =
                new UpdateTransformOutputData(
                        t.getX(),
                        t.getY(),
                        t.getScaleX(),   // assume uniform scale for UI
                        t.getRotation()
                );

        presenter.presentTransform(outputData);
    }
}
