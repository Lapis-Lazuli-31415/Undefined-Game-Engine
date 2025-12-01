package interface_adapter.trigger.action.edit;

import interface_adapter.trigger.action.ActionEditorState;
import interface_adapter.trigger.action.ActionEditorViewModel;
import use_case.trigger.action.edit.ActionEditOutputBoundary;
import use_case.trigger.action.edit.ActionEditOutputData;

public class ActionEditPresenter implements ActionEditOutputBoundary {
    private final ActionEditorViewModel actionEditorViewModel;

    public ActionEditPresenter(ActionEditorViewModel actionEditorViewModel) {
        this.actionEditorViewModel = actionEditorViewModel;
    }

    @Override
    public void prepareSuccessView(ActionEditOutputData outputData) {
        ActionEditorState state = actionEditorViewModel.getState();
        state.setAction(outputData.getScript());

        actionEditorViewModel.firePropertyChange();
    }
}
