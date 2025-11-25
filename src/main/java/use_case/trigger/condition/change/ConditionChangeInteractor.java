package use_case.trigger.condition.change;

import entity.GameObject;
import entity.scripting.condition.Condition;
import entity.scripting.condition.ConditionFactory;
import entity.scripting.condition.DefaultConditionFactory;
import view.HomeView;

public class ConditionChangeInteractor implements ConditionChangeInputBoundary {

    private final ConditionChangeOutputBoundary conditionChangePresenter;

    public ConditionChangeInteractor(ConditionChangeOutputBoundary presenter) {
        this.conditionChangePresenter = presenter;
    }

    @Override
    public void execute(ConditionChangeInputData inputData) {
        // TODO: Connect to the current editing GameObject
        GameObject gameObject = HomeView.getDemoGameObject();

        int triggerIndex = inputData.getTriggerIndex();
        int conditionIndex = inputData.getConditionIndex();

        ConditionFactory conditionFactory = new DefaultConditionFactory();
        Condition condition = conditionFactory.create(inputData.getCondition());

        gameObject.getTriggerManager().getTrigger(triggerIndex).setCondition(conditionIndex, condition);

        ConditionChangeOutputData outputData =
                new ConditionChangeOutputData(triggerIndex, conditionIndex, inputData.getCondition());

        conditionChangePresenter.prepareSuccessView(outputData);
    }
}
