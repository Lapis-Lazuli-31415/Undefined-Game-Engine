package interface_adapter.trigger.action.create;

import use_case.trigger.action.create.ActionCreateInputBoundary;
import use_case.trigger.action.create.ActionCreateInputData;

public class ActionCreateController {
    private final ActionCreateInputBoundary interactor;

    public ActionCreateController(ActionCreateInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(int triggerIndex) {
        interactor.execute(new ActionCreateInputData(triggerIndex));
    }
}
