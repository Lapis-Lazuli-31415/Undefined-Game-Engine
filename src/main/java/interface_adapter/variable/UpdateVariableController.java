package interface_adapter.variable;

import use_case.variable.UpdateVariableInputBoundary;
import use_case.variable.UpdateVariableInputData;


public class UpdateVariableController {

    private final UpdateVariableInputBoundary updateInteractor;
    private boolean editingGameController = false;

    public UpdateVariableController(UpdateVariableInputBoundary updateInteractor) {
        this.updateInteractor = updateInteractor;
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

        updateInteractor.execute(input);
    }

}
