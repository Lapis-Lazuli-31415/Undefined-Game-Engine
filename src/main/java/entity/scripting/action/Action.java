package entity.scripting.action;

import entity.scripting.environment.Environment;
import entity.scripting.error.ParseSyntaxException;

public abstract class Action {
    public abstract void execute(Environment globalEnvironment, Environment localEnvironment) throws Exception;
    public abstract void parse(String string) throws ParseSyntaxException;
    public abstract String format();
}
