package use_case.trigger.action.edit;

import entity.GameObject;
import entity.scripting.action.Action;
import interface_adapter.EditorState;

public class ActionEditInteractor implements ActionEditInputBoundary{
    private final ActionEditOutputBoundary actionEditPresenter;

    public ActionEditInteractor(ActionEditOutputBoundary actionEditPresenter) {
        this.actionEditPresenter = actionEditPresenter;
    }

    @Override
    public void execute(ActionEditInputData inputData) {

        GameObject gameObject = EditorState.getCurrentGameObject();

        int triggerIndex = inputData.getTriggerIndex();
        int actionIndex = inputData.getActionIndex();
        Action action = gameObject.getTriggerManager().getTrigger(triggerIndex).getAction(actionIndex);

        String script = action.format();

        ActionEditOutputData outputData = new ActionEditOutputData(script);
        actionEditPresenter.prepareSuccessView(outputData);
    }
}
