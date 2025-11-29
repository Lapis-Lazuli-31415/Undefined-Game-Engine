package entity.scripting.action;

import entity.scripting.environment.Environment;
import entity.scripting.error.ParseSyntaxException;

public class EmptyAction extends Action{
    public static final String ACTION_TYPE = "Empty";

    public static String getEventType() {
        return ACTION_TYPE;
    }

    @Override
    public void execute(Environment globalEnvironment, Environment localEnvironment){}

    @Override
    public void parse(String string) throws ParseSyntaxException {
        if (string != null && !string.isEmpty()) {
            throw new ParseSyntaxException("Invalid Syntax: Empty Action expects empty string");
        }
    }

    @Override
    public String format() {
        return "";
    }
}
