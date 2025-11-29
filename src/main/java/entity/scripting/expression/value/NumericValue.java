package entity.scripting.expression.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import entity.scripting.environment.Environment;
import entity.scripting.expression.NumericExpression;

public class NumericValue implements NumericExpression {
    private final double value;

    // Tells Jackson how to construct this object from JSON
    @JsonCreator
    public NumericValue(@JsonProperty("value") double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public Double evaluate(Environment globalEnvironment, Environment localEnvironment) {
        return value;
    }
}