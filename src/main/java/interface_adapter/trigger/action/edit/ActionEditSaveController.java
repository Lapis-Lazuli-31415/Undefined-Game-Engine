package interface_adapter.trigger.action.edit;

import use_case.trigger.action.edit.ActionEditSaveInputBoundary;
import use_case.trigger.action.edit.ActionEditSaveInputData;

public class ActionEditSaveController {
    private final ActionEditSaveInputBoundary actionEditSavePresenter;

    public ActionEditSaveController(ActionEditSaveInputBoundary actionEditSavePresenter) {
        this.actionEditSavePresenter = actionEditSavePresenter;
    }

    public void execute(int triggerIndex, int actionIndex, String script) {
        ActionEditSaveInputData inputData = new ActionEditSaveInputData(triggerIndex, actionIndex, script);
        actionEditSavePresenter.execute(inputData);
    }
}
