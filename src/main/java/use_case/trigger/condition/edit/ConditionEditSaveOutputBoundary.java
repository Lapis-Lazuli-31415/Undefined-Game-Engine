package use_case.trigger.condition.edit;

public interface ConditionEditSaveOutputBoundary {
    void prepareSuccessView();
    void prepareFailureView(String errorMessage);
}
