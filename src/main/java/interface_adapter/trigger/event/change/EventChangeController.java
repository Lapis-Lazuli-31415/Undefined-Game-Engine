package interface_adapter.trigger.event.change;

import use_case.trigger.event.change.EventChangeInputBoundary;
import use_case.trigger.event.change.EventChangeInputData;

public class EventChangeController {
    private final EventChangeInputBoundary eventChangeInteractor;

    public EventChangeController(EventChangeInputBoundary eventChangeInteractor){
        this.eventChangeInteractor = eventChangeInteractor;
    }

    public void execute(int index, String event){
        EventChangeInputData inputData = new EventChangeInputData(index, event);

        eventChangeInteractor.execute(inputData);
    }
}
