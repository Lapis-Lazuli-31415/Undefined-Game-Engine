package use_case.trigger.create;

import java.util.List;

public class TriggerCreateOutputData {
    private final String event;
    private final List<String> conditions;
    private final List<String> actions;

    public TriggerCreateOutputData(String event, List<String> conditions, List<String> actions) {
        this.event = event;
        this.conditions = conditions;
        this.actions = actions;
    }

    public String getEvent() {
        return event;
    }

    public List<String> getConditions() {
        return conditions;
    }

    public List<String> getActions() {
        return actions;
    }
}
