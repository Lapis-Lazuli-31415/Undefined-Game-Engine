package use_case.saving;

public interface SaveProjectOutputBoundary {
    void prepareSuccessView(SaveProjectOutputData outputData);

    void prepareFailView(String error);
}
