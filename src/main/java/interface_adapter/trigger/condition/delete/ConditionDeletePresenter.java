package interface_adapter.trigger.condition.delete;

import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import use_case.trigger.condition.delete.ConditionDeleteOutputBoundary;
import use_case.trigger.condition.delete.ConditionDeleteOutputData;

public class ConditionDeletePresenter implements ConditionDeleteOutputBoundary {
    private final TriggerManagerViewModel triggerManagerViewModel;

    public ConditionDeletePresenter(TriggerManagerViewModel triggerManagerViewModel) {
        this.triggerManagerViewModel = triggerManagerViewModel;
    }

    @Override
    public void prepareSuccessView(ConditionDeleteOutputData outputData) {
        TriggerManagerState state = triggerManagerViewModel.getState();

        state.deleteTriggerCondition(outputData.getTriggerIndex(), outputData.getConditionIndex());

        triggerManagerViewModel.firePropertyChange();
    }
}
