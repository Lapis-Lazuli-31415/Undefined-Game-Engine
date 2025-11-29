package entity.scripting.expression.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import entity.scripting.environment.Environment;
import entity.scripting.expression.BooleanExpression;

public class BooleanValue implements BooleanExpression {
    private final boolean value;

    // Tells Jackson how to construct this object from JSON
    @JsonCreator
    public BooleanValue(@JsonProperty("value") boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Boolean evaluate(Environment globalEnvironment, Environment localEnvironment) {
        return value;
    }
}