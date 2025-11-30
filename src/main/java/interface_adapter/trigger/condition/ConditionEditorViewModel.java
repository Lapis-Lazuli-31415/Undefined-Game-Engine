package interface_adapter.trigger.condition;

import interface_adapter.ViewModel;

public class ConditionEditorViewModel extends ViewModel<ConditionEditorState> {
    public ConditionEditorViewModel() {
        super("Condition Editor");
        setState(new ConditionEditorState());
    }
}
