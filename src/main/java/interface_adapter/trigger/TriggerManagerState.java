package interface_adapter.trigger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TriggerManagerState {
    private final List<TriggerState> triggers;

    public TriggerManagerState() {
        triggers = new ArrayList<>();
    }

    public void addTrigger(String event, Map<String, String> eventParameters,
                           List<String> conditions, List<String> actions) {
        TriggerState triggerState = new TriggerState(event, eventParameters, conditions, actions);
        triggers.add(triggerState);
    }

    public void deleteTrigger(int index){
        triggers.remove(index);
    }

    public String getTriggerEvent(int index){
        return triggers.get(index).getEvent();
    }

    public void setTriggerEvent(int index, String event){
        triggers.get(index).setEvent(event);
    }

    public Map<String, String> getTriggerEventParameters(int index){
        return triggers.get(index).getEventParameters();
    }

    public void setTriggerEventParameters(int index, Map<String, String> eventParameters){
        triggers.get(index).setEventParameters(eventParameters);
    }

    public void addTriggerEventParameter(int index, String key, String value){
        triggers.get(index).addEventParameter(key, value);
    }

    public void deleteTriggerEventParameter(int index, String key){
        triggers.get(index).deleteEventParameter(key);
    }

    public String getTriggerEventParameter(int index, String key){
        return Objects.requireNonNullElse(triggers.get(index).getEventParameters().get(key), "");
    }

    public void clearTriggerEventParameters(int index){
        triggers.get(index).clearEventParameters();
    }

    public List<String> getTriggerConditions(int index){
        return triggers.get(index).getConditions();
    }

    public List<String> getTriggerActions(int index){
        return triggers.get(index).getActions();
    }

    private static class TriggerState {
        private String event;
        private Map<String, String> eventParameters;
        private List<String> conditions;
        private List<String> actions;

        public TriggerState(String event, Map<String, String> eventParameters,
                            List<String> conditions, List<String> action) {
            this.event = event;
            this.eventParameters = eventParameters;
            this.conditions = conditions;
            this.actions = action;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public Map<String, String> getEventParameters() {
            return eventParameters;
        }

        public void setEventParameters(Map<String, String> eventParameters) {
            this.eventParameters = eventParameters;
        }

        public void addEventParameter(String key, String value) {
            eventParameters.put(key, value);
        }

        public void deleteEventParameter(String key) {
            eventParameters.remove(key);
        }

        public String getEventParameter(String key) {
            return eventParameters.get(key);
        }

        public void clearEventParameters() {
            eventParameters.clear();
        }

        public List<String> getConditions() {
            return conditions;
        }

        public void setConditions(List<String> conditions) {
            this.conditions = conditions;
        }

        public List<String> getActions() {
            return actions;
        }

        public void setActions(List<String> actions) {
            this.actions = actions;
        }
    }
}


