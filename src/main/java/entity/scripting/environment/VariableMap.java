package entity.scripting.environment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

public class VariableMap<T> {
    private final Map<String, T> variables;

    public VariableMap() {
        variables = new LinkedHashMap<>();
    }

    // constructor for Jackson deserialization
    @JsonCreator
    public VariableMap(@JsonProperty("variables") Map<String, T> variables) {
        this.variables = variables != null ? variables : new LinkedHashMap<>();
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

}
