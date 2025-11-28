package interface_adapter.trigger.condition.create;

import use_case.trigger.condition.create.ConditionCreateInputBoundary;
import use_case.trigger.condition.create.ConditionCreateInputData;

public class ConditionCreateController {
    private final ConditionCreateInputBoundary conditionCreateInteractor;

    public ConditionCreateController(ConditionCreateInputBoundary conditionCreateInteractor) {
        this.conditionCreateInteractor = conditionCreateInteractor;
    }

    public void execute(int index) {
        ConditionCreateInputData inputData = new ConditionCreateInputData(index);

        conditionCreateInteractor.execute(inputData);
    }
}
