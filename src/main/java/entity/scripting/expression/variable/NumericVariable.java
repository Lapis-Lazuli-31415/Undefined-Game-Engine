package entity.scripting.expression.variable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import entity.scripting.environment.Environment;
import entity.scripting.error.EnvironmentException;
import entity.scripting.error.ParseSyntaxException;
import entity.scripting.expression.Expression;
import entity.scripting.expression.ExpressionFactory;
import entity.scripting.expression.NumericExpression;

// This stops the crash by ignoring fields in the JSON that don't match Java fields
@JsonIgnoreProperties(ignoreUnknown = true)
public class NumericVariable extends Variable<Double> implements NumericExpression {
    public final static String VARIABLE_TYPE = "Numeric";
    public final static Class<Double> VALUE_TYPE = Double.class;
    public final static String KEY_WORD = "number_var";

    @JsonCreator
    public NumericVariable(@JsonProperty("name") String name,
                           @JsonProperty("isGlobal") boolean isGlobal) {
        super(name, isGlobal);
    }

    @Override
    public String getVariableType() {
        return VARIABLE_TYPE;
    }

    @Override
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

    public static NumericVariable parse(String string) throws ParseSyntaxException {
        String[] parts = ExpressionFactory.split(string).toArray(new String[0]);
        final int requiredLength = 2;

        if (parts.length != requiredLength) {
            throw new ParseSyntaxException("Invalid Syntax (NumericVariable): expected " + requiredLength +
                    " parameters but got " + parts.length);
        }

        String name = parts[0].trim();
        String scope = parts[1].trim();

        if (scope.equals("global")) {
            return new NumericVariable(name, true);
        } else if (scope.equals("local")) {
            return new NumericVariable(name, false);
        } else {
            throw new ParseSyntaxException("Invalid Syntax: " + scope + " is not a valid scope");
        }
    }

    @Override
    public String format() {
        if (isGlobal) {
            return KEY_WORD + ":(" + name + ", global)";
        } else {
            return KEY_WORD + ":(" + name + ", local)";
        }
    }
}
