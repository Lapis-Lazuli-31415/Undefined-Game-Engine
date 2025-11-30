package interface_adapter.trigger.condition.edit;

import interface_adapter.trigger.condition.ConditionEditorState;
import interface_adapter.trigger.condition.ConditionEditorViewModel;
import use_case.trigger.condition.edit.ConditionEditOutputBoundary;
import use_case.trigger.condition.edit.ConditionEditOutputData;

public class ConditionEditPresenter implements ConditionEditOutputBoundary {
    private final ConditionEditorViewModel conditionEditorViewModel;

    public ConditionEditPresenter(ConditionEditorViewModel conditionEditorViewModel) {
        this.conditionEditorViewModel = conditionEditorViewModel;
    }

    @Override
    public void prepareSuccessView(ConditionEditOutputData outputData) {
        ConditionEditorState state = conditionEditorViewModel.getState();
        state.setCondition(outputData.getScript());

        conditionEditorViewModel.firePropertyChange();
    }
}
