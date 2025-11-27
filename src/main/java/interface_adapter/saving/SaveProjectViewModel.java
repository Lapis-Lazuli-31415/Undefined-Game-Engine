package interface_adapter.saving;

import interface_adapter.ViewModel;

public class SaveProjectViewModel extends ViewModel<SaveProjectState> {

    public SaveProjectViewModel() {
        super("save_project");
        this.setState(new SaveProjectState());
    }
}