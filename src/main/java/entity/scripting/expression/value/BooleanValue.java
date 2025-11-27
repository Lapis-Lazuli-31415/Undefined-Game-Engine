package entity.scripting.expression.value;

import entity.scripting.environment.Environment;
import entity.scripting.expression.BooleanExpression;

public class BooleanValue implements BooleanExpression {
    private final boolean value;

    public BooleanValue(boolean value) {
        this.value = value;
    }

    @Override
    public Boolean evaluate(Environment globalEnvironment, Environment localEnvironment) {
        return value;
    }
}
