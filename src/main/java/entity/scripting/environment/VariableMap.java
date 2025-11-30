package entity.scripting.environment;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class VariableMap<T> {
    private final Map<String, T> variables;

    public VariableMap() {
        variables = new LinkedHashMap<>();
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

    public boolean contains(String name){
        return variables.containsKey(name);
    }

    // needed for Jackson to see and save the internal map
    public Map<String, T> getVariables() {
        return variables;
    }

    // to iterate through the variables
    public Set<String> getNames() {
        return variables.keySet();
    }

}
