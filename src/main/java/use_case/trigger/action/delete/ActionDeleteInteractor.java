package use_case.trigger.action.delete;

import entity.GameObject;
import view.HomeView;

public class ActionDeleteInteractor implements ActionDeleteInputBoundary {

    private final ActionDeleteOutputBoundary actionDeletePresenter;

    public ActionDeleteInteractor(ActionDeleteOutputBoundary actionDeletePresenter) {
        this.actionDeletePresenter = actionDeletePresenter;
    }

    @Override
    public void execute(ActionDeleteInputData inputData) {
        // TODO: Connect to the current editing GameObject
        GameObject gameObject = HomeView.getDemoGameObject();

        gameObject.getTriggerManager().getTrigger(inputData.getTriggerIndex()).deleteAction(inputData.getActionIndex());

        ActionDeleteOutputData outputData =
                new ActionDeleteOutputData(inputData.getTriggerIndex(), inputData.getActionIndex());

        actionDeletePresenter.prepareSuccessView(outputData);
    }
}
