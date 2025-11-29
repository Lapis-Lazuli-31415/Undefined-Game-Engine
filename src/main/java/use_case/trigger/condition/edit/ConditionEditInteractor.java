package use_case.trigger.condition.edit;

import entity.GameObject;
import entity.scripting.condition.Condition;
import use_case.trigger.condition.delete.ConditionDeleteOutputData;
import view.HomeView;

public class ConditionEditInteractor implements ConditionEditInputBoundary{
    private final ConditionEditOutputBoundary conditionEditPresenter;

    public ConditionEditInteractor(ConditionEditOutputBoundary conditionEditPresenter) {
        this.conditionEditPresenter = conditionEditPresenter;
    }

    @Override
    public void execute(ConditionEditInputData inputData) {
        // TODO: Connect to the current editing GameObject
        GameObject gameObject = HomeView.getDemoGameObject();

        int triggerIndex = inputData.getTriggerIndex();
        int conditionIndex = inputData.getConditionIndex();
        Condition condition = gameObject.getTriggerManager().getTrigger(triggerIndex).getCondition(conditionIndex);

        String script = condition.format();

        ConditionEditOutputData outputData = new ConditionEditOutputData(script);
        conditionEditPresenter.prepareSuccessView(outputData);
    }
}
