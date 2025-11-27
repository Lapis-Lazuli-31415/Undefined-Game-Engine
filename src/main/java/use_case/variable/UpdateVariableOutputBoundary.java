package use_case.variable;


public interface UpdateVariableOutputBoundary {

    void prepareSuccessView(UpdateVariableOutputData data);

    void prepareFailureView(String errorMessage);
}
