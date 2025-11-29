package entity.scripting.condition;

import entity.scripting.environment.Environment;
import entity.scripting.error.ParseSyntaxException;

public abstract class Condition {
    public abstract boolean evaluate(Environment globalEnvironment, Environment localEnvironment) throws Exception;
    public abstract void parse(String string) throws ParseSyntaxException;
    public abstract String format();
}
