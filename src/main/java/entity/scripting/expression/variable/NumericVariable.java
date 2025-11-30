package entity.scripting.expression.variable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import entity.scripting.environment.Environment;
import entity.scripting.error.EnvironmentException;
import entity.scripting.expression.NumericExpression;

// This stops the crash by ignoring fields in the JSON that don't match Java fields
@JsonIgnoreProperties(ignoreUnknown = true)
public class NumericVariable extends Variable<Double> implements NumericExpression {
    public final static String VARIABLE_TYPE = "Numeric";
    public final static Class<Double> VALUE_TYPE = Double.class;

    @JsonCreator
    public NumericVariable(@JsonProperty("name") String name,
                           @JsonProperty("isGlobal") boolean isGlobal) {
        super(name, isGlobal);
    }

    @Override
    @JsonIgnore // Prevents "variable_type" from being written to JSON
    public String getVariableType() {
        return VARIABLE_TYPE;
    }

    @Override
    @JsonIgnore // Prevents "value_type" from being written to JSON
    public Class<Double> getValueType() {
        return VALUE_TYPE;
    }

    @Override
    public Double evaluate(Environment globalEnvironment, Environment localEnvironment) throws EnvironmentException {
        if (isGlobal){
            return globalEnvironment.get(this);
        } else {
            return localEnvironment.get(this);
        }
    }
}