package interface_adapter.trigger.event.parameter_change;

import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import use_case.trigger.event.parameter_change.EventParameterChangeOutputBoundary;
import use_case.trigger.event.parameter_change.EventParameterChangeOutputData;

public class EventParameterChangePresenter implements EventParameterChangeOutputBoundary {
    private final TriggerManagerViewModel triggerManagerViewModel;

    public EventParameterChangePresenter(TriggerManagerViewModel triggerManagerViewModel) {
        this.triggerManagerViewModel = triggerManagerViewModel;
    }

    @Override
    public void prepareSuccessView(EventParameterChangeOutputData outputData) {
        TriggerManagerState state = triggerManagerViewModel.getState();

        state.addTriggerEventParameter(
                outputData.getIndex(), outputData.getParameterName(), outputData.getParameterValue());

        triggerManagerViewModel.firePropertyChange();
    }
}
