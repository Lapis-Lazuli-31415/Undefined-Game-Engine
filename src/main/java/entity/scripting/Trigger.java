package entity.scripting;

import entity.scripting.action.Action;
import entity.scripting.condition.Condition;
import entity.scripting.event.Event;

import java.util.List;
import java.util.ArrayList;

public class Trigger {
    private Event event;
    private List<Condition> conditions;
    private List<Action> actions;
    private boolean active;

    public Trigger(Event event, boolean active) {
        this.event = event;
        conditions = new ArrayList<>();
        actions = new ArrayList<>();
        this.active = active;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    public void setCondition(int index, Condition condition) {
        conditions.set(index, condition);
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void setAction(int index, Action action) {
        actions.set(index, action);
    }

    public Condition getCondition(int index) {
        return conditions.get(index);
    }

    public Action getAction(int index) {
        return actions.get(index);
    }

    public void deleteCondition(Condition condition) {
        conditions.remove(condition);
    }

    public void deleteCondition(int index){
        conditions.remove(index);
    }

    public void deleteAction(Action action) {
        actions.remove(action);
    }

    public void deleteAction(int index){
        actions.remove(index);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    // --- Copy method for preview isolation ---

    /**
     * Create a copy of this Trigger.
     * Event, Conditions, and Actions are shared (they define behavior, not state).
     *
     * @return A new Trigger with copied state
     */
    public Trigger copy() {
        Trigger copy = new Trigger(this.event, this.active);
        for (Condition c : this.conditions) {
            copy.addCondition(c);
        }
        for (Action a : this.actions) {
            copy.addAction(a);
        }
        return copy;
    }
}
