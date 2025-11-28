package use_case.trigger.action.create;

public class ActionCreateInputData {
    private final int triggerIndex;

    public ActionCreateInputData(int triggerIndex) {
        this.triggerIndex = triggerIndex;
    }

    public int getTriggerIndex() {
        return triggerIndex;
    }
}
