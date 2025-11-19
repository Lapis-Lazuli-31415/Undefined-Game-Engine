package app;

import entity.GameObject;
import interface_adapter.transform.TransformController;
import interface_adapter.transform.TransformPresenter;
import interface_adapter.transform.TransformViewModel;
import use_case.transform.UpdateTransformInputBoundary;
import use_case.transform.UpdateTransformInteractor;
import use_case.transform.UpdateTransformOutputBoundary;

/**
 * Factory for constructing the Transform use case
 */
public class TransformUseCaseFactory {

    private TransformUseCaseFactory() {
        // static factory class
    }

    public static TransformController create(GameObject gameObject,
                                             TransformViewModel viewModel) {

        UpdateTransformOutputBoundary presenter =
                new TransformPresenter(viewModel);

        UpdateTransformInputBoundary interactor =
                new UpdateTransformInteractor(gameObject, presenter);

        return new TransformController(interactor);
    }
}
