package entity.scripting.environment;

import entity.scripting.error.EnvironmentException;
import entity.scripting.expression.variable.Variable;

public class Assign {
    public static <T> void assign(Environment environment, Variable<T> variable, T value)
            throws EnvironmentException {
        environment.set(variable.getVariableType(), variable.getName(), variable.getValueType(), value);
    }
}
