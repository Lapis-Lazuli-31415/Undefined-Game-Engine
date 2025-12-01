package use_case.transform;

import entity.GameObject;
import entity.Transform;

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
            return;
        }

        Transform t = gameObject.getTransform();
        if (t == null) {
            return;
        }

        t.setX(data.x);
        t.setY(data.y);
        t.setScaleX(data.scale);
        t.setScaleY(data.scale);
        t.setRotation(data.rotation);

        UpdateTransformOutputData outputData =
                new UpdateTransformOutputData(
                        t.getX(),
                        t.getY(),
                        t.getScaleX(),
                        t.getRotation()
                );

        presenter.presentTransform(outputData);
    }
}
