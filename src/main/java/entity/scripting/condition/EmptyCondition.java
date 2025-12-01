package entity.scripting.condition;

import entity.scripting.environment.Environment;
import entity.scripting.error.ParseSyntaxException;

public class EmptyCondition extends Condition{
    public static final String CONDITION_TYPE = "Empty";

    @Override
    public String getConditionType() {
        return CONDITION_TYPE;
    }

    @Override
    public boolean evaluate(Environment globalEnvironment, Environment localEnvironment) {
        return true;
    }

    @Override
    public void parse(String string) throws ParseSyntaxException {
        if (string != null && !string.isEmpty()) {
            throw new ParseSyntaxException("Invalid Syntax: Empty Condition expects empty string");
        }
    }

    @Override
    public String format() {
        return "";
    }
}
