package entity.scripting.expression;

import entity.scripting.environment.Environment;
import entity.scripting.error.EnvironmentException;

public abstract class Expression<T> {
    public abstract T evaluate(Environment globalEnvironment, Environment localEnvironment) throws Exception;
}
