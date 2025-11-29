package interface_adapter.variable;

import use_case.variable.DeleteVariableInputBoundary;
import use_case.variable.DeleteVariableInputData;

public class DeleteVariableController {

    private final DeleteVariableInputBoundary localInteractor;
    private final DeleteVariableInputBoundary globalInteractor;

    public DeleteVariableController(DeleteVariableInputBoundary localInteractor,
                                    DeleteVariableInputBoundary globalInteractor) {
        this.localInteractor = localInteractor;
        this.globalInteractor = globalInteractor;
    }

    public void deleteVariable(String name,
                               String type,
                               boolean isGlobal) {

        DeleteVariableInputData input = new DeleteVariableInputData(
                name,
                isGlobal,
                type
        );

        if (isGlobal) {
            globalInteractor.execute(input);
        } else {
            localInteractor.execute(input);
        }
    }
}