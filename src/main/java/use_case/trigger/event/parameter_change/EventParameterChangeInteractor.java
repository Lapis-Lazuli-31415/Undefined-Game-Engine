package use_case.trigger.event.parameter_change;

import entity.GameObject;
import entity.scripting.Trigger;
import entity.scripting.event.DefaultEventFactory;
import entity.scripting.event.Event;
import entity.scripting.event.EventFactory;
import use_case.trigger.event.change.EventChangeInputData;
import use_case.trigger.event.change.EventChangeOutputBoundary;
import use_case.trigger.event.change.EventChangeOutputData;
import view.HomeView;

public class EventParameterChangeInteractor implements EventParameterChangeInputBoundary{
    private final EventParameterChangeOutputBoundary eventParameterChangePresenter;

    public EventParameterChangeInteractor(EventParameterChangeOutputBoundary eventParameterChangePresenter){
        this.eventParameterChangePresenter = eventParameterChangePresenter;
    }

    @Override
    public void execute(EventParameterChangeInputData inputData){
        // TODO: Connect to the current editing GameObject
        GameObject gameObject = HomeView.getDemoGameObject();

        int index = inputData.getIndex();
        Trigger trigger = gameObject.getTriggerManager().getTrigger(index);
        Event event = trigger.getEvent();
        event.addEventParameter(inputData.getParameterName(),  inputData.getParameterValue());

        EventParameterChangeOutputData outputData =
                new EventParameterChangeOutputData(index, inputData.getParameterName(), inputData.getParameterValue());
        eventParameterChangePresenter.prepareSuccessView(outputData);
    }
}

