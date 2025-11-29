package interface_adapter.trigger.condition.edit;

import use_case.trigger.condition.edit.ConditionEditSaveInputBoundary;
import use_case.trigger.condition.edit.ConditionEditSaveInputData;

public class ConditionEditSaveController {
    private final ConditionEditSaveInputBoundary conditionEditSavePresenter;

    public ConditionEditSaveController(ConditionEditSaveInputBoundary conditionEditSavePresenter) {
        this.conditionEditSavePresenter = conditionEditSavePresenter;
    }

    public void execute(int triggerIndex, int conditionIndex, String script) {
        ConditionEditSaveInputData inputData = new ConditionEditSaveInputData(triggerIndex, conditionIndex, script);
        conditionEditSavePresenter.execute(inputData);
    }
}
