package interface_adapter.saving;

import use_case.saving.SaveProjectOutputBoundary;
import use_case.saving.SaveProjectOutputData;

public class SaveProjectPresenter implements SaveProjectOutputBoundary {
    public SaveProjectPresenter(SaveProjectViewModel viewModel) {
    }
    // TODO: implement the ViewModel
    // SOMETHING LIKE: private final SaveViewModel saveViewModel;

    @Override
    public void prepareSuccessView(SaveProjectOutputData outputData) {
        // System.out.println("Success: " + outputData.getMessage());
        // TODO: update ViewModel here
    }

    @Override
    public void prepareFailView(String error) {
        System.out.println("Error: " + error);
        // TODO: update ViewModel here
    }
}