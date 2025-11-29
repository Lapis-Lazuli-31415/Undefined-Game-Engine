package entity.scripting.action;

import entity.Scene;
import entity.scripting.environment.Environment;
import entity.scripting.error.ParseSyntaxException;

public abstract class Action {
    public abstract void execute(Environment globalEnvironment, Environment localEnvironment,
                                 Scene scene) throws Exception;
    public abstract void parse(String string) throws ParseSyntaxException;
    public abstract String format();
}
