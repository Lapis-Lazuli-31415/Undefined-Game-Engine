package interface_adapter.trigger.condition.delete;

import use_case.trigger.condition.delete.ConditionDeleteInputBoundary;
import use_case.trigger.condition.delete.ConditionDeleteInputData;

public class ConditionDeleteController {
    private final ConditionDeleteInputBoundary conditionDeleteInteractor;

    public ConditionDeleteController(ConditionDeleteInputBoundary conditionDeleteInteractor) {
        this.conditionDeleteInteractor = conditionDeleteInteractor;
    }

    public void execute(int triggerIndex, int conditionIndex) {
        ConditionDeleteInputData inputData =
                new ConditionDeleteInputData(triggerIndex, conditionIndex);

        conditionDeleteInteractor.execute(inputData);
    }
}
