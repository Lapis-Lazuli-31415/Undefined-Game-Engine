package interface_adapter.trigger.action.edit;

import use_case.trigger.action.edit.ActionEditInputBoundary;
import use_case.trigger.action.edit.ActionEditInputData;

public class ActionEditController {
    private final ActionEditInputBoundary actionEditInteractor;

    public ActionEditController(ActionEditInputBoundary actionEditInteractor) {
        this.actionEditInteractor = actionEditInteractor;
    }

    public void execute(int triggerIndex, int actionIndex) {
        ActionEditInputData inputData = new ActionEditInputData(triggerIndex, actionIndex);
        actionEditInteractor.execute(inputData);
    }
}
