package use_case.component_management.create_scene;

public interface CreateSceneOutputBoundary {
    void prepareSuccessView(CreateSceneOutputData createSceneOutputData);
    void prepareFailureView(String errorMessage);
}
