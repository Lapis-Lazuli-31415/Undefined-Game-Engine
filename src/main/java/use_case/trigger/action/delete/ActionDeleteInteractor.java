package use_case.trigger.action.delete;

import entity.GameObject;
import interface_adapter.EditorState;
import view.HomeView;

public class ActionDeleteInteractor implements ActionDeleteInputBoundary {

    private final ActionDeleteOutputBoundary actionDeletePresenter;

    public ActionDeleteInteractor(ActionDeleteOutputBoundary actionDeletePresenter) {
        this.actionDeletePresenter = actionDeletePresenter;
    }

    @Override
    public void execute(ActionDeleteInputData inputData) {

        GameObject gameObject = EditorState.getCurrentGameObject();

        gameObject.getTriggerManager().getTrigger(inputData.getTriggerIndex()).deleteAction(inputData.getActionIndex());

        ActionDeleteOutputData outputData =
                new ActionDeleteOutputData(inputData.getTriggerIndex(), inputData.getActionIndex());

        actionDeletePresenter.prepareSuccessView(outputData);
    }
}
