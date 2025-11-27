package use_case.trigger.condition.change;

public class ConditionChangeOutputData {
    private final int triggerIndex;
    private final int conditionIndex;
    private final String condition;

    public ConditionChangeOutputData(int triggerIndex, int conditionIndex, String condition) {
        this.triggerIndex = triggerIndex;
        this.conditionIndex = conditionIndex;
        this.condition = condition;
    }

    public int getTriggerIndex() {
        return triggerIndex;
    }

    public int getConditionIndex() {
        return conditionIndex;
    }

    public String getCondition() {
        return condition;
    }
}
