package use_case.trigger.action.edit;

public class ActionEditSaveInputData {
    private final int triggerIndex;
    private final int actionIndex;
    private final String script;

    public ActionEditSaveInputData(int triggerIndex, int actionIndex, String script) {
        this.triggerIndex = triggerIndex;
        this.actionIndex = actionIndex;
        this.script = script;
    }

    public int getTriggerIndex() {
        return triggerIndex;
    }

    public int getActionIndex() {
        return actionIndex;
    }

    public String getScript() {
        return script;
    }
}
