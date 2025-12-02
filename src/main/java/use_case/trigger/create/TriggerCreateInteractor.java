package use_case.trigger.create;

import entity.GameObject;
import entity.scripting.Trigger;
import entity.scripting.event.EmptyEvent;
import interface_adapter.EditorState;


public class TriggerCreateInteractor implements TriggerCreateInputBoundary {
    private final TriggerCreateOutputBoundary triggerCreatePresenter;

    public TriggerCreateInteractor(TriggerCreateOutputBoundary triggerCreatePresenter){
        this.triggerCreatePresenter = triggerCreatePresenter;
    }

    @Override
    public void execute(){
        GameObject gameObject = EditorState.getCurrentGameObject();

        if (gameObject == null) {

            triggerCreatePresenter.prepareFailureView("No GameObject selected");

        } else {

            Trigger trigger = new Trigger(new EmptyEvent(), true);
            gameObject.getTriggerManager().addTrigger(trigger);

            TriggerCreateOutputData outputData = new TriggerCreateOutputData(EmptyEvent.EVENT_TYPE);
            triggerCreatePresenter.prepareSuccessView(outputData);

        }
    }
}
