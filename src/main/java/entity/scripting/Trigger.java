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

    public Trigger(Event event) {
        this.event = event;
        conditions = new ArrayList<>();
        actions = new ArrayList<>();
    }

    public Event getEvent() {
        return event;
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

    public void addAction(Action action) {
        actions.add(action);
    }

    public void deleteCondition(Condition condition) {
        conditions.remove(condition);
    }

    public void deleteAction(Action action) {
        actions.remove(action);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
