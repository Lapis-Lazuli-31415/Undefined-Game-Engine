package use_case.trigger.condition.change;

import entity.GameObject;
import entity.scripting.condition.Condition;
import entity.scripting.condition.ConditionFactory;
import entity.scripting.condition.DefaultConditionFactory;
import interface_adapter.EditorState;
import view.HomeView;

public class ConditionChangeInteractor implements ConditionChangeInputBoundary {

    private final ConditionChangeOutputBoundary conditionChangePresenter;
    private final ConditionFactory conditionFactory;

    public ConditionChangeInteractor(ConditionChangeOutputBoundary presenter, ConditionFactory presenterFactory) {
        this.conditionChangePresenter = presenter;
        this.conditionFactory = presenterFactory;
    }

    @Override
    public void execute(ConditionChangeInputData inputData) {

        GameObject gameObject = EditorState.getCurrentGameObject();

        int triggerIndex = inputData.getTriggerIndex();
        int conditionIndex = inputData.getConditionIndex();

        Condition condition = conditionFactory.create(inputData.getCondition());

        gameObject.getTriggerManager().getTrigger(triggerIndex).setCondition(conditionIndex, condition);

        ConditionChangeOutputData outputData =
                new ConditionChangeOutputData(triggerIndex, conditionIndex, inputData.getCondition());

        conditionChangePresenter.prepareSuccessView(outputData);
    }
}
