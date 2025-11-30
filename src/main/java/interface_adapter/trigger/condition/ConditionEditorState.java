package interface_adapter.trigger.condition;

import java.util.List;

public class ConditionEditorState {
    private String condition;
    private String errorMessage;

    public ConditionEditorState() {
        this.condition = "";
        this.errorMessage = "";
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
