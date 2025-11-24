package interface_adapter.trigger.create;

import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import use_case.trigger.create.TriggerCreateOutputBoundary;
import use_case.trigger.create.TriggerCreateOutputData;

public class TriggerCreatePresenter implements TriggerCreateOutputBoundary {
    private final TriggerManagerViewModel triggerManagerViewModel;

    public TriggerCreatePresenter(TriggerManagerViewModel triggerManagerViewModel) {
        this.triggerManagerViewModel = triggerManagerViewModel;
    }

    public void prepareView(TriggerCreateOutputData outputData) {
        TriggerManagerState state = triggerManagerViewModel.getState();

        state.addTrigger(outputData.getEvent(), outputData.getConditions(), outputData.getActions());

        triggerManagerViewModel.firePropertyChange();
    }
}
