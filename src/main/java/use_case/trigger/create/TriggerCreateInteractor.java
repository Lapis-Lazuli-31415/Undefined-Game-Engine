package use_case.trigger.create;

import entity.GameObject;
import entity.scripting.Trigger;
import entity.scripting.event.EmptyEvent;
import view.HomeView;

import java.util.ArrayList;
import java.util.HashMap;

public class TriggerCreateInteractor implements ConditionCreateInputBoundary {
    private final TriggerCreateOutputBoundary triggerCreatePresenter;

    public TriggerCreateInteractor(TriggerCreateOutputBoundary triggerCreatePresenter){
        this.triggerCreatePresenter = triggerCreatePresenter;
    }

    @Override
    public void execute(){
        // TODO: Connect to the current editing GameObject
        GameObject gameObject = HomeView.getDemoGameObject();

        Trigger trigger = new Trigger(new EmptyEvent(), true);
        gameObject.getTriggerManager().addTrigger(trigger);

        TriggerCreateOutputData outputData = new TriggerCreateOutputData(EmptyEvent.EVENT_TYPE);
        triggerCreatePresenter.prepareSuccessView(outputData);

    }
}
