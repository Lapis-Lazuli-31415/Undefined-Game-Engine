package use_case.trigger.condition.delete;

import entity.GameObject;
import interface_adapter.EditorState;

public class ConditionDeleteInteractor implements ConditionDeleteInputBoundary {
    private final ConditionDeleteOutputBoundary conditionDeletePresenter;

    public ConditionDeleteInteractor(ConditionDeleteOutputBoundary conditionDeletePresenter) {
        this.conditionDeletePresenter = conditionDeletePresenter;
    }

    @Override
    public void execute(ConditionDeleteInputData inputData) {

        GameObject gameObject = EditorState.getCurrentGameObject();

        int triggerIndex = inputData.getTriggerIndex();
        int conditionIndex = inputData.getConditionIndex();
        gameObject.getTriggerManager().getTrigger(triggerIndex).deleteCondition(conditionIndex);

        ConditionDeleteOutputData outputData =
                new ConditionDeleteOutputData(triggerIndex, conditionIndex);
        conditionDeletePresenter.prepareSuccessView(outputData);
    }
}

