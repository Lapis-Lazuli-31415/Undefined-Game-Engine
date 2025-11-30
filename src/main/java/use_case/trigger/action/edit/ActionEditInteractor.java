package use_case.trigger.action.edit;

import entity.GameObject;
import entity.scripting.action.Action;
import use_case.trigger.action.edit.ActionEditInputData;
import use_case.trigger.action.edit.ActionEditOutputBoundary;
import use_case.trigger.action.edit.ActionEditOutputData;
import view.HomeView;

public class ActionEditInteractor implements ActionEditInputBoundary{
    private final ActionEditOutputBoundary actionEditPresenter;

    public ActionEditInteractor(ActionEditOutputBoundary actionEditPresenter) {
        this.actionEditPresenter = actionEditPresenter;
    }

    @Override
    public void execute(ActionEditInputData inputData) {
        // TODO: Connect to the current editing GameObject
        GameObject gameObject = HomeView.getDemoGameObject();

        int triggerIndex = inputData.getTriggerIndex();
        int actionIndex = inputData.getActionIndex();
        Action action = gameObject.getTriggerManager().getTrigger(triggerIndex).getAction(actionIndex);

        String script = action.format();

        ActionEditOutputData outputData = new ActionEditOutputData(script);
        actionEditPresenter.prepareSuccessView(outputData);
    }
}
