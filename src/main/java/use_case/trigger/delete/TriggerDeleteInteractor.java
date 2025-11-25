package use_case.trigger.delete;

import entity.GameObject;
import view.HomeView;

public class TriggerDeleteInteractor implements TriggerDeleteInputBoundary {
    private final TriggerDeleteOutputBoundary triggerDeletePresenter;

    public TriggerDeleteInteractor(TriggerDeleteOutputBoundary triggerDeletePresenter){
        this.triggerDeletePresenter = triggerDeletePresenter;
    }

    @Override
    public void execute(TriggerDeleteInputData inputData){
        // TODO: Connect to the current editing GameObject
        GameObject gameObject = HomeView.getDemoGameObject();

        int index = inputData.getIndex();
        gameObject.getTriggerManager().deleteTrigger(index);

        TriggerDeleteOutputData outputData = new TriggerDeleteOutputData(index);
        triggerDeletePresenter.prepareSuccessView(outputData);

    }
}
