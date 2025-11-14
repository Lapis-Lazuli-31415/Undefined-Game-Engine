package entity.scripting;

import entity.scripting.action.Action;
import entity.scripting.condition.Condition;
import entity.scripting.event.Event;
import entity.scripting.event.EmptyEvent;

import java.util.ArrayList;

public class Trigger {
    private Event event;
    private ArrayList<Condition> conditions;
    private ArrayList<Action> actions;

    public Trigger(String eventName) {
        event = new EmptyEvent();
        conditions = new ArrayList<>();
        actions = new ArrayList<>();
    }

    public Event getEvent() {
        return event;
    }

    public ArrayList<Condition> getConditions() {
        return conditions;
    }

    public ArrayList<Action> getActions() {
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
}
