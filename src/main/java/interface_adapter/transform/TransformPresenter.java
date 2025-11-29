package interface_adapter.transform;

import use_case.transform.UpdateTransformOutputBoundary;
import use_case.transform.UpdateTransformOutputData;

/**
 * Presenter converts UpdateTransformOutputData into a TransformViewModel.
 */
public class TransformPresenter implements UpdateTransformOutputBoundary {

    private final TransformViewModel viewModel;

    public TransformPresenter(TransformViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentTransform(UpdateTransformOutputData data) {
        TransformState state = viewModel.getState();

        state.setX(data.getX());
        state.setY(data.getY());
        state.setScale(data.getScale());      // assuming uniform scale
        state.setRotation(data.getRotation());

        // Notify observers once after updating the entire state
        viewModel.firePropertyChange();
    }
}
