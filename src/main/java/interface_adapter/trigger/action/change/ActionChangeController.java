package interface_adapter.trigger.action.change;

import use_case.trigger.action.change.ActionChangeInputBoundary;
import use_case.trigger.action.change.ActionChangeInputData;

public class ActionChangeController {

    private final ActionChangeInputBoundary actionChangeInteractor;

    public ActionChangeController(ActionChangeInputBoundary actionChangeInteractor) {
        this.actionChangeInteractor = actionChangeInteractor;
    }

    public void execute(int triggerIndex, int actionIndex, String action) {
        ActionChangeInputData inputData =
                new ActionChangeInputData(triggerIndex, actionIndex, action);

        actionChangeInteractor.execute(inputData);
    }
}
