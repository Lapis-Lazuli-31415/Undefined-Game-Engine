package interface_adapter.trigger.action.delete;

import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import use_case.trigger.action.delete.ActionDeleteOutputBoundary;
import use_case.trigger.action.delete.ActionDeleteOutputData;

public class ActionDeletePresenter implements ActionDeleteOutputBoundary {

    private final TriggerManagerViewModel triggerManagerViewModel;

    public ActionDeletePresenter(TriggerManagerViewModel triggerManagerViewModel) {
        this.triggerManagerViewModel = triggerManagerViewModel;
    }

    @Override
    public void prepareSuccessView(ActionDeleteOutputData outputData) {
        TriggerManagerState state = triggerManagerViewModel.getState();

        state.deleteTriggerAction(outputData.getTriggerIndex(), outputData.getActionIndex());
        triggerManagerViewModel.firePropertyChange();
    }
}
