package use_case.trigger.create;

import entity.GameObject;
import entity.Property;
import entity.scripting.Trigger;
import entity.scripting.environment.Environment;
import entity.scripting.event.EmptyEvent;

import java.util.ArrayList;

public class TriggerCreateInteractor implements TriggerCreateInputBoundary{
    private final TriggerCreateOutputBoundary triggerCreatePresenter;

    public TriggerCreateInteractor(TriggerCreateOutputBoundary triggerCreatePresenter){
        this.triggerCreatePresenter = triggerCreatePresenter;
    }

    @Override
    public void execute(){
        // TODO: Connect to the current editing GameObject
        GameObject gameObject = new GameObject("bear-1", "bear", true, new ArrayList<Property>(), new Environment());

        Trigger trigger = new Trigger(new EmptyEvent(), true);
        gameObject.getTriggerManager().addTrigger(trigger);

        TriggerCreateOutputData outputData = new TriggerCreateOutputData(EmptyEvent.getEventType(),
                new ArrayList<String>(), new ArrayList<String>());
        triggerCreatePresenter.prepareView(outputData);

    }
}
