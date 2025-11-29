package use_case.trigger.action.edit;

public interface ActionEditSaveOutputBoundary {
    void prepareSuccessView();
    void prepareFailureView(String errorMessage);
}
