package interface_adapter.variable.update;

import use_case.variable.update.UpdateVariableInputBoundary;
import use_case.variable.update.UpdateVariableInputData;

public class UpdateVariableController {

    private final UpdateVariableInputBoundary localInteractor;
    private final UpdateVariableInputBoundary globalInteractor;

    public UpdateVariableController(UpdateVariableInputBoundary localInteractor,
                                    UpdateVariableInputBoundary globalInteractor) {
        this.localInteractor = localInteractor;
        this.globalInteractor = globalInteractor;
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

        if (isGlobal) {
            globalInteractor.execute(input);
        } else {
            localInteractor.execute(input);
        }
    }
}