package use_case.trigger.delete;

import entity.GameObject;
import interface_adapter.EditorState;
import view.HomeView;

public class TriggerDeleteInteractor implements TriggerDeleteInputBoundary {
    private final TriggerDeleteOutputBoundary triggerDeletePresenter;

    public TriggerDeleteInteractor(TriggerDeleteOutputBoundary triggerDeletePresenter){
        this.triggerDeletePresenter = triggerDeletePresenter;
    }

    @Override
    public void execute(TriggerDeleteInputData inputData){

        GameObject gameObject = EditorState.getCurrentGameObject();

        int index = inputData.getIndex();
        gameObject.getTriggerManager().deleteTrigger(index);

        TriggerDeleteOutputData outputData = new TriggerDeleteOutputData(index);
        triggerDeletePresenter.prepareSuccessView(outputData);

    }
}
