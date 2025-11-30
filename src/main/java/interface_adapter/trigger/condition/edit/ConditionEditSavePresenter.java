package interface_adapter.trigger.condition.edit;

import interface_adapter.trigger.condition.ConditionEditorState;
import interface_adapter.trigger.condition.ConditionEditorViewModel;
import use_case.trigger.condition.edit.ConditionEditSaveOutputBoundary;

public class ConditionEditSavePresenter implements ConditionEditSaveOutputBoundary {
    private final ConditionEditorViewModel viewModel;

    public  ConditionEditSavePresenter(ConditionEditorViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView() {
        ConditionEditorState state = viewModel.getState();
        state.setErrorMessage(null);

        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailureView(String errorMessage) {
        ConditionEditorState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.firePropertyChange();
    }
}
