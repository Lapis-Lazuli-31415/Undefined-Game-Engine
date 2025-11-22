package entity.scripting.expression.value;

import entity.scripting.environment.Environment;
import entity.scripting.expression.NumericExpression;

public class NumericValue implements NumericExpression {
    private final double value;

    public NumericValue(double value) {
        this.value = value;
    }

    @Override
    public Double evaluate(Environment globalEnvironment, Environment localEnvironment) {
        return value;
    }
}
