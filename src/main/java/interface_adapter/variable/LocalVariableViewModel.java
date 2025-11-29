package interface_adapter.variable;

import interface_adapter.ViewModel;

public class LocalVariableViewModel extends ViewModel<VariableState> {
//    private String currentGameObjectId;

    public LocalVariableViewModel() {
        super("localVariables");
        this.state = new VariableState();
    }

//    public void setCurrentGameObjectId(String gameObjectId) {
//        this.currentGameObjectId = gameObjectId;
//    }
//
//    public String getCurrentGameObjectId() {
//        return currentGameObjectId;
//    }
}