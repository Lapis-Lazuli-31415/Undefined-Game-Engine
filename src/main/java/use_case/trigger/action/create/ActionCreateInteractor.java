package use_case.trigger.action.create;

import entity.GameObject;
import entity.scripting.action.Action;
import entity.scripting.action.EmptyAction;
import interface_adapter.EditorState;

public class ActionCreateInteractor implements ActionCreateInputBoundary {

    private final ActionCreateOutputBoundary actionCreatePresenter;

    public ActionCreateInteractor(ActionCreateOutputBoundary actionCreatePresenter) {
        this.actionCreatePresenter = actionCreatePresenter;
    }

    @Override
    public void execute(ActionCreateInputData inputData) {

        GameObject gameObject = EditorState.getCurrentGameObject();

        int triggerIndex = inputData.getTriggerIndex();

        Action action = new EmptyAction();
        gameObject.getTriggerManager().getTrigger(triggerIndex).addAction(action);

        ActionCreateOutputData outputData =
                new ActionCreateOutputData(triggerIndex, EmptyAction.ACTION_TYPE);

        actionCreatePresenter.prepareSuccessView(outputData);
    }
}
