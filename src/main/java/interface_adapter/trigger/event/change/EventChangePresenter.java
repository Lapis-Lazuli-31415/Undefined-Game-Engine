package interface_adapter.trigger.event.change;

import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import use_case.trigger.event.change.EventChangeOutputBoundary;
import use_case.trigger.event.change.EventChangeOutputData;

public class EventChangePresenter implements EventChangeOutputBoundary {
    private final TriggerManagerViewModel triggerManagerViewModel;

    public EventChangePresenter(TriggerManagerViewModel triggerManagerViewModel) {
        this.triggerManagerViewModel = triggerManagerViewModel;
    }

    @Override
    public void prepareSuccessView(EventChangeOutputData outputData) {
        TriggerManagerState state = triggerManagerViewModel.getState();

        state.setTriggerEvent(outputData.getIndex(), outputData.getEvent());
        state.clearTriggerEventParameters(outputData.getIndex());

        triggerManagerViewModel.firePropertyChange();
    }
}
