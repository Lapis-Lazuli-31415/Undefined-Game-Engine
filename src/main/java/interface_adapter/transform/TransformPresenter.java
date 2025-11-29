package interface_adapter.transform;

import use_case.transform.UpdateTransformOutputBoundary;
import use_case.transform.UpdateTransformOutputData;

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
        state.setScale(data.getScale());
        state.setRotation(data.getRotation());

        viewModel.firePropertyChange();
    }
}
