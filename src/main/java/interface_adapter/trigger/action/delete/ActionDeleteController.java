package interface_adapter.trigger.action.delete;

import use_case.trigger.action.delete.ActionDeleteInputBoundary;
import use_case.trigger.action.delete.ActionDeleteInputData;

public class ActionDeleteController {

    private final ActionDeleteInputBoundary actionDeleteInteractor;

    public ActionDeleteController(ActionDeleteInputBoundary actionDeleteInteractor) {
        this.actionDeleteInteractor = actionDeleteInteractor;
    }

    public void execute(int triggerIndex, int actionIndex) {
        ActionDeleteInputData input = new ActionDeleteInputData(triggerIndex, actionIndex);
        actionDeleteInteractor.execute(input);
    }
}