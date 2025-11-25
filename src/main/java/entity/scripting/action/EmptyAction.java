package entity.scripting.action;

import entity.scripting.environment.Environment;

public class EmptyAction extends Action{
    public static final String ACTION_TYPE = "Empty";

    public static String getEventType() {
        return ACTION_TYPE;
    }

    @Override
    public void execute(Environment globalEnvironment, Environment localEnvironment){}
}
