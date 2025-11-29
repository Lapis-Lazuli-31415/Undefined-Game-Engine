package entity.scripting.expression.value;

import entity.scripting.environment.Environment;
import entity.scripting.error.ParseSyntaxException;
import entity.scripting.expression.BooleanExpression;
import entity.scripting.expression.Expression;
import entity.scripting.expression.ExpressionFactory;

import java.util.Set;

public class BooleanValue implements BooleanExpression {
    private final boolean value;
    public static final String KEY_WORD = "boolean";

    public BooleanValue(boolean value) {
        this.value = value;
    }

    @Override
    public Boolean evaluate(Environment globalEnvironment, Environment localEnvironment) {
        return value;
    }

    public static BooleanValue parse(String string) throws ParseSyntaxException {
        if (string.equals("true")) {
            return new BooleanValue(true);
        } else if (string.equals("false")) {
            return new BooleanValue(false);
        } else {
            throw new ParseSyntaxException("Invalid Syntax: " + string + " is not a boolean value");
        }
    }

    private boolean isBoolean(String string) {
        final Set<String> BOOLEANS = Set.of("true", "false");

        return BOOLEANS.contains(string);
    }

    @Override
    public String format() {
        return KEY_WORD + ":(" + value + ")";
    }
}
