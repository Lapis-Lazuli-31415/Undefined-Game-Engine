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

    // No-Argument Constructor for Jackson
    public Trigger() {
        this.event = new EmptyEvent(); // Default to avoid nulls
        this.conditions = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.active = true;
    }

    // Standard Constructor
    public Trigger(Event event, boolean active) {
        this.event = event;
        this.conditions = new ArrayList<>();
        this.actions = new ArrayList<>();
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

    // Jackson uses this to set the list
    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<Action> getActions() {
        return actions;
    }

    // Jackson uses this to set the list
    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    // --- Helper Methods ---

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
}