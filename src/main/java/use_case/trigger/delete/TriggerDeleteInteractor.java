package use_case.trigger.delete;

import entity.GameObject;
import entity.Property;
import entity.scripting.environment.Environment;

import java.util.ArrayList;

public class TriggerDeleteInteractor implements TriggerDeleteInputBoundary {
    private final TriggerDeleteOutputBoundary triggerDeletePresenter;

    public TriggerDeleteInteractor(TriggerDeleteOutputBoundary triggerDeletePresenter){
        this.triggerDeletePresenter = triggerDeletePresenter;
    }

    @Override
    public void execute(TriggerDeleteInputData inputData){
        // TODO: Connect to the current editing GameObject
        GameObject gameObject = new GameObject("bear-1", "bear", true, new ArrayList<Property>(), new Environment());

        int index = inputData.getIndex();
        gameObject.getTriggerManager().deleteTrigger(index);

        TriggerDeleteOutputData outputData = new TriggerDeleteOutputData(index);
        triggerDeletePresenter.prepareView(outputData);

    }
}
