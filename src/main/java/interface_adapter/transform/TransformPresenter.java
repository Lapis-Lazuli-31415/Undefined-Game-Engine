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
        viewModel.setX(transform.getX());
        viewModel.setY(transform.getY());
        viewModel.setScale(transform.getScaleX()); // assuming uniform scale
        viewModel.setRotation(transform.getRotation());
    }
}
