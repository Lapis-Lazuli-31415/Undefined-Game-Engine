package use_case.variable;

public interface DeleteVariableOutputBoundary {

    void prepareSuccessView(DeleteVariableOutputData data);
    void prepareFailureView(String errorMessage);

}
