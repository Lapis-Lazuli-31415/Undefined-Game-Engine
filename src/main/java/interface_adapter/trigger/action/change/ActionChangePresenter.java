package interface_adapter.trigger.action.change;

import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import use_case.trigger.action.change.ActionChangeOutputBoundary;
import use_case.trigger.action.change.ActionChangeOutputData;

public class ActionChangePresenter implements ActionChangeOutputBoundary {

    private final TriggerManagerViewModel triggerManagerViewModel;

    public ActionChangePresenter(TriggerManagerViewModel triggerManagerViewModel) {
        this.triggerManagerViewModel = triggerManagerViewModel;
    }

    @Override
    public void prepareSuccessView(ActionChangeOutputData outputData) {
        TriggerManagerState state = triggerManagerViewModel.getState();

        state.setTriggerAction(
                outputData.getTriggerIndex(),
                outputData.getActionIndex(),
                outputData.getAction()
        );

        triggerManagerViewModel.firePropertyChange();
    }
}
