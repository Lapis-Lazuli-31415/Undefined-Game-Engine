package interface_adapter.trigger.action;

import interface_adapter.ViewModel;
import interface_adapter.trigger.action.ActionEditorState;

public class ActionEditorViewModel extends ViewModel<ActionEditorState> {
    public ActionEditorViewModel() {
        super("Action Editor");
        setState(new ActionEditorState());
    }
}
