package use_case.trigger.condition.create;

import entity.GameObject;
import entity.scripting.condition.Condition;
import entity.scripting.condition.EmptyCondition;
import view.HomeView;

public class ConditionCreateInteractor implements ConditionCreateInputBoundary{
    private final ConditionCreateOutputBoundary conditionCreatePresenter;

    public ConditionCreateInteractor(ConditionCreateOutputBoundary conditionCreatePresenter){
        this.conditionCreatePresenter = conditionCreatePresenter;
    }

    @Override
    public void execute(ConditionCreateInputData inputData){
        // TODO: Connect to the current editing GameObject
        GameObject gameObject = HomeView.getDemoGameObject();

        int index = inputData.getIndex();
        Condition condition = new EmptyCondition();
        gameObject.getTriggerManager().getTrigger(index).addCondition(condition);

        ConditionCreateOutputData outputData = new ConditionCreateOutputData(index, EmptyCondition.EVENT_TYPE);
        conditionCreatePresenter.prepareSuccessView(outputData);

    }
}
