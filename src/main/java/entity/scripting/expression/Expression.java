package entity.scripting.expression;

import entity.scripting.environment.Environment;
import entity.scripting.error.ParseSyntaxException;

public interface Expression<T> {
    T evaluate(Environment globalEnvironment, Environment localEnvironment) throws Exception;
    String format();
}
