package interface_adapter.trigger.event.parameter_change;

import use_case.trigger.event.change.EventChangeInputBoundary;
import use_case.trigger.event.change.EventChangeInputData;
import use_case.trigger.event.parameter_change.EventParameterChangeInputBoundary;
import use_case.trigger.event.parameter_change.EventParameterChangeInputData;

public class EventParameterChangeController {
    private final EventParameterChangeInputBoundary eventParameterChangeInteractor;

    public EventParameterChangeController(EventParameterChangeInputBoundary eventParameterChangeInteractor){
        this.eventParameterChangeInteractor = eventParameterChangeInteractor;
    }

    public void execute(int index, String parameterName, String parameterValue){
        EventParameterChangeInputData inputData =
                new EventParameterChangeInputData(index, parameterName, parameterValue);

        eventParameterChangeInteractor.execute(inputData);
    }
}
