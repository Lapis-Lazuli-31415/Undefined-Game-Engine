package interface_adapter.transform;

import interface_adapter.ViewModel;

/**
 * View model for the Transform.
 * Wraps a TransformState and notifies observers on change.
 */
public class TransformViewModel extends ViewModel<TransformState> {

    public TransformViewModel() {
        super("transform");
        this.state = new TransformState();
    }
}
