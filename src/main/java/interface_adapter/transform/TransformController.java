package interface_adapter.transform;

import use_case.transform.UpdateTransformInputBoundary;
import use_case.transform.UpdateTransformInputData;

public class TransformController {

    private final UpdateTransformInputBoundary interactor;

    public TransformController(UpdateTransformInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void updateTransform(double x, double y, double scale, float rotation) {
        UpdateTransformInputData data = new UpdateTransformInputData(x, y, scale, rotation);
        interactor.updateTransform(data);
    }
}
