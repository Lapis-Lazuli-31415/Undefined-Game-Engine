package entity.scripting.condition;

import entity.scripting.event.Event;

import java.util.List;

public interface ConditionFactory {
    Condition create(String type);
    List<String> getRegisteredConditions();
}
