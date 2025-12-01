package interface_adapter.selection;

import interface_adapter.ViewModel;


public class SelectionViewModel extends ViewModel<SelectionState> {

    public static final String VIEW_NAME = "scene-selection";

    public SelectionViewModel() {
        super(VIEW_NAME);
        this.state = new SelectionState();
    }

    public void setSelection(String id, String name) {
        state.setSelectedObjectId(id);
        state.setSelectedObjectName(name);
        fireSelectionChanged();
    }


    public void fireSelectionChanged() {
        firePropertyChange();
    }
}
