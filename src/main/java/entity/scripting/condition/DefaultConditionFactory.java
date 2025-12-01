package entity.scripting.condition;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultConditionFactory implements ConditionFactory{
    private final Map<String, Supplier<Condition>> registry = new LinkedHashMap<>();

    public DefaultConditionFactory() {
        registry.put(EmptyCondition.CONDITION_TYPE, EmptyCondition::new);
        registry.put(NumericComparisonCondition.CONDITION_TYPE, NumericComparisonCondition::new);
        registry.put(BooleanComparisonCondition.CONDITION_TYPE, BooleanComparisonCondition::new);
    }

    @Override
    public Condition create(String type) {
        Supplier<Condition> supplier = registry.get(type);
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown condition type: " + type);
        }
        return supplier.get();
    }

    @Override
    public List<String> getRegisteredConditions() {
        return List.copyOf(registry.keySet());
    }
}
