package interface_adapter.trigger.delete;

import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import use_case.trigger.delete.TriggerDeleteOutputBoundary;
import use_case.trigger.delete.TriggerDeleteOutputData;

public class TriggerDeletePresenter implements TriggerDeleteOutputBoundary {
    private final TriggerManagerViewModel triggerManagerViewModel;

    public TriggerDeletePresenter(TriggerManagerViewModel triggerManagerViewModel) {
        this.triggerManagerViewModel = triggerManagerViewModel;
    }

    @Override
    public void prepareSuccessView(TriggerDeleteOutputData outputData) {
        TriggerManagerState state = triggerManagerViewModel.getState();

        state.deleteTrigger(outputData.getIndex());

        triggerManagerViewModel.firePropertyChange();
    }
}
