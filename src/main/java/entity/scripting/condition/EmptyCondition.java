package entity.scripting.condition;

import entity.scripting.environment.Environment;

import java.util.List;

public class EmptyCondition extends Condition{
    public static final String EVENT_TYPE = "Empty";

    public static String getEventType() {
        return EVENT_TYPE;
    }

    @Override
    public boolean evaluate(Environment globalEnvironment, Environment localEnvironment) {
        return true;
    }
}
