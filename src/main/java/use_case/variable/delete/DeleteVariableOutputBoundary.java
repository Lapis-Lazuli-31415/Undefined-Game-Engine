package use_case.variable.delete;

public interface DeleteVariableOutputBoundary {

    void prepareSuccessView(DeleteVariableOutputData data);
    void prepareFailureView(String errorMessage);

}
