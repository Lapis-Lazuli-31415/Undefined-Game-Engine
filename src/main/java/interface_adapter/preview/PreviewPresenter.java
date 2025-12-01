package interface_adapter.preview;

import use_case.preview.PreviewOutputBoundary;
import use_case.preview.PreviewOutputData;

/**
 * Presenter for preview feature.
 * Part of Interface Adapter layer (green ring in CA diagram).
 * Formats Use Case output for the View.
 *
 * Converts Output Data (simple types) to View Model state.
 *
 * @author Wanru Cheng
 */
public class PreviewPresenter implements PreviewOutputBoundary {

    private final PreviewViewModel viewModel;

    public PreviewPresenter(PreviewViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(PreviewOutputData outputData) {
        PreviewState state = new PreviewState();

        // Use simple types from Output Data
        state.setSceneId(outputData.getSceneId());
        state.setSceneName(outputData.getSceneName());
        state.setGameObjectCount(outputData.getGameObjectCount());
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

        // Use simple types from Output Data
        state.setSceneId(outputData.getSceneId());
        state.setSceneName(outputData.getSceneName());
        state.setGameObjectCount(outputData.getGameObjectCount());
        state.setWarning(warningMessage);
        state.setReadyToPreview(true);

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}