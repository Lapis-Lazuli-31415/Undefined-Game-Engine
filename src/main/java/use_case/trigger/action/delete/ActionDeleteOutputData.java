package use_case.trigger.action.delete;

public class ActionDeleteOutputData {
    private final int triggerIndex;
    private final int actionIndex;

    public ActionDeleteOutputData(int triggerIndex, int actionIndex) {
        this.triggerIndex = triggerIndex;
        this.actionIndex = actionIndex;
    }

    public int getTriggerIndex() { return triggerIndex; }
    public int getActionIndex() { return actionIndex; }
}