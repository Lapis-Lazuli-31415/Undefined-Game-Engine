package use_case.trigger.condition.edit;

public class ConditionEditInputData {
    private final int triggerIndex;
    private final int conditionIndex;

    public ConditionEditInputData(int triggerIndex, int conditionIndex) {
        this.triggerIndex = triggerIndex;
        this.conditionIndex = conditionIndex;
    }

    public int getTriggerIndex() {
        return triggerIndex;
    }

    public int getConditionIndex() {
        return conditionIndex;
    }
}
