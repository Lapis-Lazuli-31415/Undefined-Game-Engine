package use_case.trigger.condition.create;

import entity.GameObject;
import entity.scripting.condition.Condition;
import entity.scripting.condition.EmptyCondition;
import interface_adapter.EditorState;

public class ConditionCreateInteractor implements ConditionCreateInputBoundary{
    private final ConditionCreateOutputBoundary conditionCreatePresenter;

    public ConditionCreateInteractor(ConditionCreateOutputBoundary conditionCreatePresenter){
        this.conditionCreatePresenter = conditionCreatePresenter;
    }

    @Override
    public void execute(ConditionCreateInputData inputData){

        GameObject gameObject = EditorState.getCurrentGameObject();

        int index = inputData.getIndex();
        Condition condition = new EmptyCondition();
        gameObject.getTriggerManager().getTrigger(index).addCondition(condition);

        ConditionCreateOutputData outputData = new ConditionCreateOutputData(index, EmptyCondition.CONDITION_TYPE);
        conditionCreatePresenter.prepareSuccessView(outputData);

    }
}
