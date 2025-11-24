package interface_adapter.trigger;

import java.util.ArrayList;
import java.util.List;

public class TriggerManagerState {
    private final List<TriggerState> triggers;

    public TriggerManagerState() {
        triggers = new ArrayList<>();
    }

    public void addTrigger(String event, List<String> conditions, List<String> actions) {
        TriggerState triggerState = new TriggerState(event, conditions, actions);
        triggers.add(triggerState);
    }

    public void deleteTrigger(int index){
        triggers.remove(index);
    }

    public String getTriggerEvent(int index){
        return triggers.get(index).getEvent();
    }

    public List<String> getTriggerConditions(int index){
        return triggers.get(index).getConditions();
    }

    public List<String> getTriggerActions(int index){
        return triggers.get(index).getActions();
    }

    private static class TriggerState {
        private String event;
        private List<String> conditions;
        private List<String> actions;

        public TriggerState(String event, List<String> conditions, List<String> action) {
            this.event = event;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
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


