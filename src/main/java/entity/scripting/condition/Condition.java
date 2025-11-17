package entity.scripting.condition;

import entity.scripting.environment.Environment;

public abstract class Condition {
    public abstract boolean evaluate(Environment globalEnvironment, Environment localEnvironment) throws Exception;
}
