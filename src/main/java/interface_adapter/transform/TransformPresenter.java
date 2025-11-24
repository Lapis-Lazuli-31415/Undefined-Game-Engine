package interface_adapter.transform;

import entity.Transform;
import use_case.transform.UpdateTransformOutputBoundary;

/**
 * Presenter converts entity Transform into a TransformViewModel.
 */
public class TransformPresenter implements UpdateTransformOutputBoundary {

    private final TransformViewModel viewModel;

    public TransformPresenter(TransformViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentTransform(Transform transform) {
        TransformState state = viewModel.getState();

        state.setX(transform.getX());
        state.setY(transform.getY());
        state.setScale(transform.getScaleX()); // assuming uniform scale
        state.setRotation(transform.getRotation());

        // Notify observers once after updating the entire state
        viewModel.firePropertyChange();
    }
}
