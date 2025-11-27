package use_case.saving;

public interface SaveProjectOutputBoundary {
    /**
     * Prepares the success view with the relevant output data.
     * This method is called by the Interactor when the project has been
     * successfully saved.
     * @param outputData The data required to present the success state (e.g., success message).
     */

    void prepareSuccessView(SaveProjectOutputData outputData);

    void prepareFailView(String error);
}
