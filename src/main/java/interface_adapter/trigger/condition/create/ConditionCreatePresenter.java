package interface_adapter.trigger.condition.create;

import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import use_case.trigger.condition.create.ConditionCreateOutputBoundary;
import use_case.trigger.condition.create.ConditionCreateOutputData;

public class ConditionCreatePresenter implements ConditionCreateOutputBoundary {
    private final TriggerManagerViewModel triggerManagerViewModel;

    public ConditionCreatePresenter(TriggerManagerViewModel triggerManagerViewModel) {
        this.triggerManagerViewModel = triggerManagerViewModel;
    }

    public void prepareSuccessView(ConditionCreateOutputData outputData) {
        TriggerManagerState state = triggerManagerViewModel.getState();

        state.addTriggerCondition(outputData.getIndex(), outputData.getCondition());

        triggerManagerViewModel.firePropertyChange();
    }
}
