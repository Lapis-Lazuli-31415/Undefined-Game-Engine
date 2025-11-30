package interface_adapter.trigger.condition.edit;

import use_case.trigger.condition.edit.ConditionEditInputBoundary;
import use_case.trigger.condition.edit.ConditionEditInputData;

public class ConditionEditController {
    private final ConditionEditInputBoundary conditionEditInteractor;

    public ConditionEditController(ConditionEditInputBoundary conditionEditInteractor) {
        this.conditionEditInteractor = conditionEditInteractor;
    }

    public void execute(int triggerIndex, int conditionIndex) {
        ConditionEditInputData inputData = new ConditionEditInputData(triggerIndex, conditionIndex);
        conditionEditInteractor.execute(inputData);
    }
}
