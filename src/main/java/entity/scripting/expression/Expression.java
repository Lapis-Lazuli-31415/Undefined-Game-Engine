package entity.scripting.expression;

import entity.scripting.environment.Environment;

public interface Expression<T> {
    public abstract T evaluate(Environment globalEnvironment, Environment localEnvironment) throws Exception;
}
