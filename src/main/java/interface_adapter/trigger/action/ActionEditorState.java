package interface_adapter.trigger.action;

public class ActionEditorState {
    private String action;
    private String errorMessage;

    public ActionEditorState() {
        this.action = "";
        this.errorMessage = "";
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
