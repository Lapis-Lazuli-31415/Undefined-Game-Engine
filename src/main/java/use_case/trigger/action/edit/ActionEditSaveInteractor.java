package use_case.trigger.action.edit;

import entity.GameObject;
import entity.scripting.action.Action;
import entity.scripting.error.ParseSyntaxException;
import interface_adapter.EditorState;

public class ActionEditSaveInteractor implements ActionEditSaveInputBoundary {
    private final ActionEditSaveOutputBoundary actionEditSavePresenter;

    public ActionEditSaveInteractor(ActionEditSaveOutputBoundary actionEditSavePresenter) {
        this.actionEditSavePresenter = actionEditSavePresenter;
    }

    @Override
    public void execute(ActionEditSaveInputData inputData) {

        GameObject gameObject = EditorState.getCurrentGameObject();

        int triggerIndex = inputData.getTriggerIndex();
        int actionIndex = inputData.getActionIndex();
        String script = inputData.getScript();

        try {
            Action action =  gameObject.getTriggerManager().getTrigger(triggerIndex).getAction(actionIndex);
            action.parse(script);
            actionEditSavePresenter.prepareSuccessView();

        } catch (ParseSyntaxException err) {
            actionEditSavePresenter.prepareFailureView(err.getMessage());
        }
    }
}
