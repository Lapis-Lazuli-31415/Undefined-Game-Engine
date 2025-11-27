package interface_adapter.variable;

import interface_adapter.ViewModel;


public class VariableViewModel extends ViewModel<VariableState> {

    public VariableViewModel() {
        super("variables");
        this.state = new VariableState();
    }
}
