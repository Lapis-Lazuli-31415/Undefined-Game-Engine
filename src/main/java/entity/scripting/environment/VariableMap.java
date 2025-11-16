package entity.scripting.environment;

import java.util.HashMap;
import java.util.Map;

public class VariableMap<T> {
    private Map<String, T> variables;

    public VariableMap() {
        variables = new HashMap<>();
    }

    public void set(String name, T value){
        variables.put(name, value);
    }

    public T get(String name){
        return variables.get(name);
    }

    public void delete(String name){
        variables.remove(name);
    }
}
