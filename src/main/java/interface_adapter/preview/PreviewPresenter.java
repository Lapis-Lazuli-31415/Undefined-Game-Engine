package interface_adapter.preview;

import use_case.preview.PreviewOutputBoundary;
import use_case.preview.PreviewOutputData;

/**
 * Presenter for preview feature.
 * Part of Interface Adapter layer (green ring in CA diagram).
 * Formats Use Case output for the View.
 *
 * @author Wanru Cheng
 */
public class PreviewPresenter implements PreviewOutputBoundary {

    private final PreviewViewModel viewModel;

    /**
     * Constructor.
     *
     * @param viewModel The view model to update
     */
    public PreviewPresenter(PreviewViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(PreviewOutputData outputData) {
        PreviewState state = new PreviewState();
        state.setScene(outputData.getScene());
        state.setError(null);
        state.setWarning(outputData.getWarning());
        state.setReadyToPreview(true);

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentError(String errorMessage) {
        PreviewState state = new PreviewState();
        state.setError(errorMessage);
        state.setReadyToPreview(false);

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentWarning(String warningMessage, PreviewOutputData outputData) {
        PreviewState state = new PreviewState();
        state.setScene(outputData.getScene());
        state.setWarning(warningMessage);
        state.setReadyToPreview(true);

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}