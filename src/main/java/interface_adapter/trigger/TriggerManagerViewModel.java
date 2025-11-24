package interface_adapter.trigger;

import interface_adapter.ViewModel;
import interface_adapter.transform.TransformState;

public class TriggerManagerViewModel extends ViewModel<TriggerManagerState> {
    public TriggerManagerViewModel() {
        super("Trigger Manager");
        setState(new TriggerManagerState());
    }
}
