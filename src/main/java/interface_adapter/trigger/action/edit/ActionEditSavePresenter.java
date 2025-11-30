package interface_adapter.trigger.action.edit;

import interface_adapter.trigger.action.ActionEditorState;
import interface_adapter.trigger.action.ActionEditorViewModel;
import use_case.trigger.action.edit.ActionEditOutputBoundary;
import use_case.trigger.action.edit.ActionEditSaveOutputBoundary;

public class ActionEditSavePresenter implements ActionEditSaveOutputBoundary {
    private final ActionEditorViewModel viewModel;

    public  ActionEditSavePresenter(ActionEditorViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView() {
        ActionEditorState state = viewModel.getState();
        state.setErrorMessage(null);

        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailureView(String errorMessage) {
        ActionEditorState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.firePropertyChange();
    }
}
