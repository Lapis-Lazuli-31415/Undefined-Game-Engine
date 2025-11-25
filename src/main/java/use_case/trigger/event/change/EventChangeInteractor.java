package use_case.trigger.event.change;

import entity.GameObject;
import entity.scripting.Trigger;
import entity.scripting.event.DefaultEventFactory;
import entity.scripting.event.Event;
import entity.scripting.event.EventFactory;
import view.HomeView;


public class EventChangeInteractor implements EventChangeInputBoundary {
    private final EventChangeOutputBoundary eventChangePresenter;

    public EventChangeInteractor(EventChangeOutputBoundary eventChangePresenter){
        this.eventChangePresenter = eventChangePresenter;
    }

    @Override
    public void execute(EventChangeInputData inputData){
        // TODO: Connect to the current editing GameObject
        GameObject gameObject = HomeView.getDemoGameObject();

        int index = inputData.getIndex();
        Trigger trigger = gameObject.getTriggerManager().getTrigger(index);
        EventFactory eventFactory = new DefaultEventFactory();
        Event event = eventFactory.create(inputData.getEvent());
        trigger.setEvent(event);

        EventChangeOutputData outputData = new EventChangeOutputData(index, event.getEventLabel());
        eventChangePresenter.prepareSuccessView(outputData);
    }
}
