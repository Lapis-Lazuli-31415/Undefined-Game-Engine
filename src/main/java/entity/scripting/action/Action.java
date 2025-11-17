package entity.scripting.action;

import entity.scripting.environment.Environment;

public abstract class Action {
    public abstract void execute(Environment globalEnvironment, Environment localEnvironment) throws Exception;
}
