package use_case.trigger.condition.change;

public class ConditionChangeInputData {
    private final int triggerIndex;
    private final int conditionIndex;
    private final String condition;

    public ConditionChangeInputData(int triggerIndex, int conditionIndex, String condition) {
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
