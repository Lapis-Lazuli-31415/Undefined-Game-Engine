package interface_adapter.variable;

import interface_adapter.ViewModel;

public class GlobalVariableViewModel extends ViewModel<VariableState> {

    public GlobalVariableViewModel() {
        super("globalVariables");
        this.state = new VariableState();
    }
}