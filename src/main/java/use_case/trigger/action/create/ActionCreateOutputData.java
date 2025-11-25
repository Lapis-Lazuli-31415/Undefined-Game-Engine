package use_case.trigger.action.create;

public class ActionCreateOutputData {
    private final int triggerIndex;
    private final String actionType;

    public ActionCreateOutputData(int triggerIndex, String actionType) {
        this.triggerIndex = triggerIndex;
        this.actionType = actionType;
    }

    public int getTriggerIndex() {
        return triggerIndex;
    }

    public String getActionType() {
        return actionType;
    }
}
