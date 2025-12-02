package use_case.trigger.event.change;

import entity.GameObject;
import entity.scripting.Trigger;
import entity.scripting.event.Event;
import entity.scripting.event.EventFactory;
import interface_adapter.EditorState;


public class EventChangeInteractor implements EventChangeInputBoundary {
    private final EventChangeOutputBoundary eventChangePresenter;
    private final EventFactory eventFactory;

    public EventChangeInteractor(EventChangeOutputBoundary eventChangePresenter, EventFactory eventFactory) {
        this.eventChangePresenter = eventChangePresenter;
        this.eventFactory = eventFactory;
    }

    @Override
    public void execute(EventChangeInputData inputData){

        GameObject gameObject = EditorState.getCurrentGameObject();

        int index = inputData.getIndex();
        Trigger trigger = gameObject.getTriggerManager().getTrigger(index);

        Event event = eventFactory.create(inputData.getEvent());
        trigger.setEvent(event);

        EventChangeOutputData outputData = new EventChangeOutputData(index, inputData.getEvent());
        eventChangePresenter.prepareSuccessView(outputData);
    }
}
