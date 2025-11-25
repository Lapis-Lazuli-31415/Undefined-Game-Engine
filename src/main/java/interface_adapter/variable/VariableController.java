package interface_adapter.variable;

import use_case.variable.UpdateVariableInputBoundary;
import use_case.variable.UpdateVariableInputData;
import use_case.variable.DeleteVariableInputBoundary;
import use_case.variable.DeleteVariableInputData;


public class VariableController {

    private final UpdateVariableInputBoundary updateInteractor;
    private final DeleteVariableInputBoundary deleteInteractor;

    public VariableController(UpdateVariableInputBoundary updateInteractor,
                              DeleteVariableInputBoundary deleteInteractor) {
        this.updateInteractor = updateInteractor;
        this.deleteInteractor = deleteInteractor;
    }


    public void updateVariable(String name,
                               String type,
                               boolean isGlobal,
                               String value) {

        UpdateVariableInputData input = new UpdateVariableInputData(
                name,
                value,
                isGlobal,
                type
        );

        updateInteractor.execute(input);   // or whatever your method name is
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
