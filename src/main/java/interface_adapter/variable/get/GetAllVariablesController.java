package interface_adapter.variable.get;

import use_case.variable.get.GetAllVariablesInputBoundary;
import use_case.variable.get.GetAllVariablesInputData;

public class GetAllVariablesController {

    private final GetAllVariablesInputBoundary globalInteractor;
    private final GetAllVariablesInputBoundary localInteractor;

    public GetAllVariablesController(
            GetAllVariablesInputBoundary globalInteractor,
            GetAllVariablesInputBoundary localInteractor) {
        this.globalInteractor = globalInteractor;
        this.localInteractor = localInteractor;
    }


    public void refreshGlobalVariables() {
        GetAllVariablesInputData inputData = new GetAllVariablesInputData(true);
        globalInteractor.getAllVariables(inputData);
    }

    public void refreshLocalVariables() {
        GetAllVariablesInputData inputData = new GetAllVariablesInputData(false);
        localInteractor.getAllVariables(inputData);
    }
}