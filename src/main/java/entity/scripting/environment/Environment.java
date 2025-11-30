package entity.scripting.environment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import entity.scripting.error.EnvironmentException;
import entity.scripting.expression.variable.Variable;

import java.util.TreeMap;
import java.util.Map;

public class Environment {
    private final Map<String, VariableMap<?>> variables;

    public Environment(){
        variables = new TreeMap<>();
    }

    @JsonCreator
    public Environment(@JsonProperty("variables") Map<String, VariableMap<?>> variables) {
        this.variables = variables != null ? variables : new TreeMap<>();
    }

    protected <T> void set(Variable<T> variable, T value) throws EnvironmentException {
        String variableType = variable.getVariableType();
        String name = variable.getName();
        Class<T> valueType = variable.getValueType();

        if (!valueType.isInstance(value)){
            throw new EnvironmentException(
                    "Invalid value for variable " + name + ": expected type "
                            + valueType.getSimpleName() + " but received "
                            + value.getClass().getSimpleName()
            );
        }

        if (!variables.containsKey(variableType)){
            variables.put(variableType, new VariableMap<T>());
        }

        @SuppressWarnings("unchecked")
        VariableMap<T> variableMap = (VariableMap<T>) variables.get(variableType);

        variableMap.set(name, value);
    }

    public <T> T get(Variable<T> variable) throws EnvironmentException {
        String variableType = variable.getVariableType();
        String name = variable.getName();
        Class<T> valueType = variable.getValueType();

        if (!variables.containsKey(variableType)){
            throw new EnvironmentException("Unknown/Empty variable category: " + variableType);
        }

        VariableMap<?> variableMap = variables.get(variableType);

        if (!variableMap.contains(name)){
            throw new EnvironmentException("Variable " + name + " not found in category " + variableType);
        }

        Object rawValue = variableMap.get(name);

        if (!valueType.isInstance(rawValue)){
            throw new EnvironmentException(
                    "Type mismatch for variable " + name + ": expected "
                            + valueType.getSimpleName() + " but found "
                            + rawValue.getClass().getSimpleName()
            );
        }

        @SuppressWarnings("unchecked")
        T value = (T) rawValue;
        return value;
    }

    public <T> void delete(Variable<T> variable) throws EnvironmentException {
        String variableType = variable.getVariableType();
        String name = variable.getName();

        if (!variables.containsKey(variableType)){
            throw new EnvironmentException("Unknown/Empty variable category: " + variableType);
        }

        VariableMap<?> variableMap = variables.get(variableType);

        variableMap.delete(name);
    }

    public <T> boolean contains(Variable<T> variable) {
        String variableType = variable.getVariableType();
        String name = variable.getName();

        if (!variables.containsKey(variableType)){
            return false;
        }

        VariableMap<?> variableMap = variables.get(variableType);

        return variableMap.contains(name);
    }

    // needed for Jackson to see and save the internal map
    public Map<String, VariableMap<?>> getVariables() {
        return variables;
    }
    // --- Copy method for preview isolation ---

    /**
     * Create a deep copy of this Environment.
     * For preview, we start with a fresh environment.
     *
     * @return A new empty Environment
     */
    public Environment copy() {
        // For preview, start fresh - variables will be set during gameplay
        return new Environment();
    }
}
