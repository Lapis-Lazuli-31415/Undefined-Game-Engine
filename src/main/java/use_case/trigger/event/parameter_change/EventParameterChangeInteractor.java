package use_case.trigger.event.parameter_change;

import entity.GameObject;
import entity.scripting.Trigger;
import entity.scripting.event.Event;
import interface_adapter.EditorState;

public class EventParameterChangeInteractor implements EventParameterChangeInputBoundary{
    private final EventParameterChangeOutputBoundary eventParameterChangePresenter;

    public EventParameterChangeInteractor(EventParameterChangeOutputBoundary eventParameterChangePresenter){
        this.eventParameterChangePresenter = eventParameterChangePresenter;
    }

    @Override
    public void execute(EventParameterChangeInputData inputData){

        GameObject gameObject = EditorState.getCurrentGameObject();

        int index = inputData.getIndex();
        Trigger trigger = gameObject.getTriggerManager().getTrigger(index);
        Event event = trigger.getEvent();
        event.addEventParameter(inputData.getParameterName(),  inputData.getParameterValue());

        EventParameterChangeOutputData outputData =
                new EventParameterChangeOutputData(index, inputData.getParameterName(), inputData.getParameterValue());
        eventParameterChangePresenter.prepareSuccessView(outputData);
    }
}

