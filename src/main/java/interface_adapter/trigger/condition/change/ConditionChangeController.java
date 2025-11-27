package interface_adapter.trigger.condition.change;

import use_case.trigger.condition.change.ConditionChangeInputBoundary;
import use_case.trigger.condition.change.ConditionChangeInputData;

public class ConditionChangeController {
    private final ConditionChangeInputBoundary conditionChangeInteractor;

    public ConditionChangeController(ConditionChangeInputBoundary conditionChangeInteractor) {
        this.conditionChangeInteractor = conditionChangeInteractor;
    }

    public void execute(int triggerIndex, int conditionIndex, String conditionType) {
        ConditionChangeInputData inputData =
                new ConditionChangeInputData(triggerIndex, conditionIndex, conditionType);

        conditionChangeInteractor.execute(inputData);
    }
}
