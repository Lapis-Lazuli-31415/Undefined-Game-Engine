package entity.scripting.expression.variable;

import entity.scripting.environment.Environment;
import entity.scripting.error.EnvironmentException;
import entity.scripting.expression.BooleanExpression;

public class BooleanVariable extends Variable<Boolean> implements BooleanExpression {
    public final static String VARIABLE_TYPE = "Boolean";
    private final static Class<Boolean> VALUE_TYPE = Boolean.class;

    public BooleanVariable(String name, boolean isGlobal) {
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
}
