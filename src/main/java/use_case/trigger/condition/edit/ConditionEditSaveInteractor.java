package use_case.trigger.condition.edit;

import entity.GameObject;
import entity.scripting.condition.Condition;
import entity.scripting.error.ParseSyntaxException;
import view.HomeView;

import java.text.ParseException;

public class ConditionEditSaveInteractor implements ConditionEditSaveInputBoundary{
    private final ConditionEditSaveOutputBoundary conditionEditSavePresenter;

    public ConditionEditSaveInteractor(ConditionEditSaveOutputBoundary conditionEditSavePresenter) {
        this.conditionEditSavePresenter = conditionEditSavePresenter;
    }

    @Override
    public void execute(ConditionEditSaveInputData inputData) {
        // TODO: Connect to the current editing GameObject
        GameObject gameObject = HomeView.getDemoGameObject();

        int triggerIndex = inputData.getTriggerIndex();
        int conditionIndex = inputData.getConditionIndex();
        String script = inputData.getScript();

        try {
            Condition condition =  gameObject.getTriggerManager().getTrigger(triggerIndex).getCondition(conditionIndex);
            condition.parse(script);
            conditionEditSavePresenter.prepareSuccessView();

        } catch (ParseSyntaxException err) {
            conditionEditSavePresenter.prepareFailureView(err.getMessage());
        }
    }
}
