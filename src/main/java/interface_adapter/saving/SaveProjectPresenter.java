package interface_adapter.saving;

import use_case.saving.SaveProjectOutputBoundary;
import use_case.saving.SaveProjectOutputData;

public class SaveProjectPresenter implements SaveProjectOutputBoundary {

    private final SaveProjectViewModel saveProjectViewModel;

    public SaveProjectPresenter(SaveProjectViewModel viewModel) {
        this.saveProjectViewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SaveProjectOutputData outputData) {
        // Update state with success message
        SaveProjectState state = saveProjectViewModel.getState();
        state.setMessage(outputData.getMessage());
        state.setError(null);

        // Notify the View (HomeView)
        saveProjectViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        SaveProjectState state = saveProjectViewModel.getState();
        state.setError(error);

        saveProjectViewModel.firePropertyChange();
    }
}