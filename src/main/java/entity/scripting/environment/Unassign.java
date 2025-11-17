package entity.scripting.environment;

import entity.scripting.error.EnvironmentException;
import entity.scripting.expression.variable.Variable;

public class Unassign {
    public static <T> void unassign(Environment environment, Variable<T> variable)
            throws EnvironmentException {
        environment.unset(variable.getVariableType(), variable.getName());
    }
}
