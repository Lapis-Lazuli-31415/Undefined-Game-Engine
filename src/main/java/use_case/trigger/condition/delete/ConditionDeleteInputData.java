package use_case.trigger.condition.delete;

public class ConditionDeleteInputData {
    private final int triggerIndex;
    private final int conditionIndex;

    public ConditionDeleteInputData(int triggerIndex, int conditionIndex) {
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