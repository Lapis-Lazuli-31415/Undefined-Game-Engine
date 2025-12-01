package use_case.variable.update;


public interface UpdateVariableOutputBoundary {

    void prepareSuccessView(UpdateVariableOutputData data);

    void prepareFailureView(String errorMessage);
}
