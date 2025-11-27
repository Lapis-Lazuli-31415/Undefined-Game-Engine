package use_case.trigger.create;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TriggerCreateOutputData {
    private final String event;
    private final Map<String, String> eventParameters;
    private final List<String> conditions;
    private final List<String> actions;

    public TriggerCreateOutputData(String event, Map<String, String> eventParameters,
                                   List<String> conditions, List<String> actions) {
        this.event = event;
        this.eventParameters = eventParameters;
        this.conditions = conditions;
        this.actions = actions;
    }

    public TriggerCreateOutputData(String event){
        this.event = event;
        this.eventParameters = new HashMap<>();
        this.conditions = new ArrayList<>();
        this.actions = new ArrayList<>();
    }

    public String getEvent() {
        return event;
    }

    public Map<String, String> getEventParameters() {
        return eventParameters;
    }

    public List<String> getConditions() {
        return conditions;
    }

    public List<String> getActions() {
        return actions;
    }
}
