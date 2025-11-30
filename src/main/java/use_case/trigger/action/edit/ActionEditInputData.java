package use_case.trigger.action.edit;

public class ActionEditInputData {
    private final int triggerIndex;
    private final int actionIndex;

    public ActionEditInputData(int triggerIndex, int actionIndex) {
        this.triggerIndex = triggerIndex;
        this.actionIndex = actionIndex;
    }

    public int getTriggerIndex() {
        return triggerIndex;
    }

    public int getActionIndex() {
        return actionIndex;
    }
}
