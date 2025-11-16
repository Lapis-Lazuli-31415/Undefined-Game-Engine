package entity.scripting.environment;

import entity.scripting.error.EnvironmentException;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, VariableMap<?>> variables;

    public Environment(){
        variables = new HashMap<>();
    }

    <T> void set(String variableType, String name, Class<T> valueType, T value) throws EnvironmentException {
        if (!valueType.isInstance(value)){
            throw new EnvironmentException("Value " + value + " is not a type of " + valueType.getSimpleName());
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
            throw new EnvironmentException("Environment does not contain type: " + variableType);
        }


        VariableMap<?> variableMap = variables.get(variableType);

        if (!valueType.isInstance(variableMap.get(name))){
            throw new EnvironmentException(
                    "Variable " + name + " does not refer to a type of " + valueType.getSimpleName());
        }

        @SuppressWarnings("unchecked")
        T value = (T) variableMap.get(name);
        return value;
    }
}
