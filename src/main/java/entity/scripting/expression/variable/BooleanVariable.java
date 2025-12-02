package entity.scripting.expression.variable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import entity.scripting.environment.Environment;
import entity.scripting.error.EnvironmentException;
import entity.scripting.error.ParseSyntaxException;
import entity.scripting.expression.BooleanExpression;
import entity.scripting.expression.Expression;
import entity.scripting.expression.ExpressionFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BooleanVariable extends Variable<Boolean> implements BooleanExpression {
    public final static String VARIABLE_TYPE = "Boolean";
    public final static Class<Boolean> VALUE_TYPE = Boolean.class;
    public final static String KEY_WORD = "boolean_var";

    @JsonCreator
    public BooleanVariable(@JsonProperty("name") String name,
                           @JsonProperty("global") boolean isGlobal) {
        super(name, isGlobal);
    }

    @Override
    public String getVariableType() {
        return VARIABLE_TYPE;
    }

    @Override
    public Class<Boolean> getValueType() {
        return VALUE_TYPE;
    }

    @Override
    public Boolean evaluate(Environment globalEnvironment, Environment localEnvironment) throws EnvironmentException {
        if (isGlobal){
            return globalEnvironment.get(this);
        } else {
            return localEnvironment.get(this);
        }
    }

    public static BooleanVariable parse(String string) throws ParseSyntaxException {
        String[] parts = ExpressionFactory.split(string).toArray(new String[0]);
        int requiredLength = 2;

        if (parts.length != requiredLength) {
            throw new ParseSyntaxException("Invalid Syntax (BooleanVariable): expected " + requiredLength +
                    " parameters but got " + parts.length);
        }

        String name = parts[0].trim();
        String scope = parts[1].trim();

        if (scope.equals("global")) {
            return new BooleanVariable(name, true);
        } else if (scope.equals("local")) {
            return new BooleanVariable(name, false);
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
