package use_case.create_scene;

public interface CreateSceneOutputBoundary {
    void prepareSuccessView(CreateSceneOutputData createSceneOutputData);
    void prepareFailureView(String errorMessage);
}
