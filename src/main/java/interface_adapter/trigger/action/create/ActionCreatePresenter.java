package interface_adapter.trigger.action.create;

import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import use_case.trigger.action.create.ActionCreateOutputBoundary;
import use_case.trigger.action.create.ActionCreateOutputData;

public class ActionCreatePresenter implements ActionCreateOutputBoundary {

    private final TriggerManagerViewModel triggerManagerViewModel;

    public ActionCreatePresenter(TriggerManagerViewModel triggerManagerViewModel) {
        this.triggerManagerViewModel = triggerManagerViewModel;
    }

    @Override
    public void prepareSuccessView(ActionCreateOutputData outputData) {
        TriggerManagerState state = triggerManagerViewModel.getState();

        state.addTriggerAction(outputData.getTriggerIndex(), outputData.getActionType());
        triggerManagerViewModel.firePropertyChange();
    }
}
