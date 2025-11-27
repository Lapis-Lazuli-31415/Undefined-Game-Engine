package entity.scripting;

import entity.scripting.action.Action;
import entity.scripting.condition.Condition;
import entity.scripting.event.Event;
import entity.scripting.event.EmptyEvent;

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
