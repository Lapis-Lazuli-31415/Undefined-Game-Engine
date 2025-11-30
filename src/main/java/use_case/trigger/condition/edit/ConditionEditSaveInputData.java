package use_case.trigger.condition.edit;

public class ConditionEditSaveInputData {
    private final int triggerIndex;
    private final int conditionIndex;
    private final String script;

    public ConditionEditSaveInputData(int triggerIndex, int conditionIndex, String script) {
        this.triggerIndex = triggerIndex;
        this.conditionIndex = conditionIndex;
        this.script = script;
    }

    public int getTriggerIndex() {
        return triggerIndex;
    }

    public int getConditionIndex() {
        return conditionIndex;
    }

    public String getScript() {
        return script;
    }
}
