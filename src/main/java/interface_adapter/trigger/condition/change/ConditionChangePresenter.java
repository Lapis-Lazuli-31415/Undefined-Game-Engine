package interface_adapter.trigger.condition.change;

import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import use_case.trigger.condition.change.ConditionChangeOutputBoundary;
import use_case.trigger.condition.change.ConditionChangeOutputData;

public class ConditionChangePresenter implements ConditionChangeOutputBoundary {

    private final TriggerManagerViewModel triggerManagerViewModel;

    public ConditionChangePresenter(TriggerManagerViewModel triggerManagerViewModel) {
        this.triggerManagerViewModel = triggerManagerViewModel;
    }

    @Override
    public void prepareSuccessView(ConditionChangeOutputData outputData) {
        TriggerManagerState state = triggerManagerViewModel.getState();

        state.setTriggerCondition(
                outputData.getTriggerIndex(),
                outputData.getConditionIndex(),
                outputData.getCondition()
        );

        triggerManagerViewModel.firePropertyChange();
    }
}