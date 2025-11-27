package use_case.variable.factory;

import entity.scripting.expression.variable.Variable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DefaultVariableFactoryRegistry {

    private final Map<String, VariableFactory>registry = new LinkedHashMap<>();

    public DefaultVariableFactoryRegistry() {
        register(new NumericVariableFactory());
        register(new BooleanVariableFactory());
    }

    public void register(VariableFactory factory) {
        registry.put(factory.getTypename(), factory);
    }

    public VariableFactory get(String typename) {
        return registry.get(typename);
    }

    public List<String> getRegisteredTypes() {
        return List.copyOf(registry.keySet());
    }

}
