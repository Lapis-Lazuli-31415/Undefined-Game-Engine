package use_case.trigger.action.change;

public class ActionChangeOutputData {
    private final int triggerIndex;
    private final int actionIndex;
    private final String action;

    public ActionChangeOutputData(int triggerIndex, int actionIndex, String action) {
        this.triggerIndex = triggerIndex;
        this.actionIndex = actionIndex;
        this.action = action;
    }

    public int getTriggerIndex() { return triggerIndex; }
    public int getActionIndex() { return actionIndex; }
    public String getAction() { return action; }
}
