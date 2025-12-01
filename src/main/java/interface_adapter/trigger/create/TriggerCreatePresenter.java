package interface_adapter.trigger.create;

import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import use_case.trigger.create.TriggerCreateOutputBoundary;
import use_case.trigger.create.TriggerCreateOutputData;

import javax.swing.*;

public class TriggerCreatePresenter implements TriggerCreateOutputBoundary {
    private final TriggerManagerViewModel triggerManagerViewModel;

    public TriggerCreatePresenter(TriggerManagerViewModel triggerManagerViewModel) {
        this.triggerManagerViewModel = triggerManagerViewModel;
    }

    public void prepareSuccessView(TriggerCreateOutputData outputData) {
        TriggerManagerState state = triggerManagerViewModel.getState();

        state.setErrorMessage(null);

        state.addTrigger(outputData.getEvent(), outputData.getEventParameters(),
                outputData.getConditions(), outputData.getActions());

        triggerManagerViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailureView(String errorMessage) {
        TriggerManagerState state = triggerManagerViewModel.getState();

        state.setErrorMessage(errorMessage);

        triggerManagerViewModel.firePropertyChange();
    }
}
