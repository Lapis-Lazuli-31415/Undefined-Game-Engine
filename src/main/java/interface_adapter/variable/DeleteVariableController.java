package interface_adapter.variable;


import use_case.variable.DeleteVariableInputBoundary;
import use_case.variable.DeleteVariableInputData;


public class DeleteVariableController {

    private final DeleteVariableInputBoundary deleteInteractor;

    public DeleteVariableController(DeleteVariableInputBoundary deleteInteractor) {
        this.deleteInteractor = deleteInteractor;
    }

    public void deleteVariable(String name,
                               String type,
                               boolean isGlobal) {

        DeleteVariableInputData input = new DeleteVariableInputData(
                name,
                isGlobal,
                type

        );

        deleteInteractor.execute(input);
    }

}
