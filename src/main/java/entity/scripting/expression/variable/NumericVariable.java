package entity.scripting.expression.variable;

import entity.scripting.environment.Environment;
import entity.scripting.error.EnvironmentException;
import entity.scripting.expression.NumericExpression;

public class NumericVariable extends NumericExpression implements Variable<Double> {
    private String name;
    private boolean isGlobal;
    private final static String VARIABLE_TYPE = "Numeric";
    private final static Class<Double> VALUE_TYPE = Double.class;

    public NumericVariable(String name, boolean isGlobal) {
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
    public Class<Double> getValueType() {
        return VALUE_TYPE;
    }

    @Override
    public Double evaluate(Environment globalEnvironment, Environment localEnvironment) throws EnvironmentException {
        if (isGlobal){
            return globalEnvironment.get(VARIABLE_TYPE, name, VALUE_TYPE);
        } else {
            return localEnvironment.get(VARIABLE_TYPE, name, VALUE_TYPE);
        }
    }
}
