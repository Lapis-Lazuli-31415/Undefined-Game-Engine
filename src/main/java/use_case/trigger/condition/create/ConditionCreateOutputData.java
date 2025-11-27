package use_case.trigger.condition.create;

public class ConditionCreateOutputData {
    private int index;
    private final String condition;

    public ConditionCreateOutputData(int index, String condition) {
        this.index = index;
        this.condition = condition;
    }

    public int getIndex() {
        return index;
    }

    public String getCondition() {
        return condition;
    }
}
