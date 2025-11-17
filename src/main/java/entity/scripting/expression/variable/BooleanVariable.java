package entity.scripting.expression.variable;

import entity.scripting.environment.Environment;
import entity.scripting.error.EnvironmentException;
import entity.scripting.expression.BooleanExpression;

public class BooleanVariable extends BooleanExpression implements Variable<Boolean> {
    private String name;
    private boolean isGlobal;
    public final static String VARIABLE_TYPE = "Boolean";
    private final static Class<Boolean> VALUE_TYPE = Boolean.class;

    public BooleanVariable(String name, boolean isGlobal) {
        this.name = name;
        this.isGlobal = isGlobal;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isGlobal() {
        return isGlobal;
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
            return globalEnvironment.get(VARIABLE_TYPE, name, VALUE_TYPE);
        } else {
            return localEnvironment.get(VARIABLE_TYPE, name, VALUE_TYPE);
        }
    }
}
