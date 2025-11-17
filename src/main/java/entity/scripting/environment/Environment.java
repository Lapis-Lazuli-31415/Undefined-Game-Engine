package entity.scripting.environment;

import entity.scripting.error.EnvironmentException;

import java.util.TreeMap;
import java.util.Map;

public class Environment {
    private final Map<String, VariableMap<?>> variables;

    public Environment(){
        variables = new TreeMap<>();
    }

    <T> void set(String variableType, String name, Class<T> valueType, T value) throws EnvironmentException {
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

    public <T> T get(String variableType, String name, Class<T> valueType) throws EnvironmentException {
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

    public <T> void unset(String variableType, String name) throws EnvironmentException {
        if (!variables.containsKey(variableType)){
            throw new EnvironmentException("Unknown/Empty variable category: " + variableType);
        }

        VariableMap<?> variableMap = variables.get(variableType);

        variableMap.unset(name);
    }
}
