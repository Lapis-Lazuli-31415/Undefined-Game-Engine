package use_case.trigger.action.change;

import entity.GameObject;
import entity.scripting.action.Action;
import entity.scripting.action.ActionFactory;
import interface_adapter.EditorState;

public class ActionChangeInteractor implements ActionChangeInputBoundary {

    private final ActionChangeOutputBoundary actionChangePresenter;
    private final ActionFactory actionFactory;

    public ActionChangeInteractor(ActionChangeOutputBoundary actionChangePresenter, ActionFactory actionFactory) {
        this.actionChangePresenter = actionChangePresenter;
        this.actionFactory = actionFactory;
    }

    @Override
    public void execute(ActionChangeInputData inputData) {

        GameObject gameObject = EditorState.getCurrentGameObject();

        Action action = actionFactory.create(inputData.getAction());

        gameObject.getTriggerManager().getTrigger(inputData.getTriggerIndex())
                .setAction(inputData.getActionIndex(), action);

        ActionChangeOutputData outputData =
                new ActionChangeOutputData(
                        inputData.getTriggerIndex(),
                        inputData.getActionIndex(),
                        inputData.getAction()
                );

        actionChangePresenter.prepareSuccessView(outputData);
    }
}
