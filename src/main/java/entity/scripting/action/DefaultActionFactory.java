package entity.scripting.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultActionFactory implements ActionFactory {

    private final Map<String, Supplier<Action>> registry = new LinkedHashMap<>();

    public DefaultActionFactory() {
        // Register built-in actions here
        registry.put(EmptyAction.ACTION_TYPE, EmptyAction::new);
        registry.put(NumericVariableAssignmentAction.ACTION_TYPE, NumericVariableAssignmentAction::new);
        registry.put(BooleanVariableAssignmentAction.ACTION_TYPE, BooleanVariableAssignmentAction::new);
        registry.put(WaitAction.ACTION_TYPE, WaitAction::new);
    }

    @Override
    public Action create(String type) {
        Supplier<Action> supplier = registry.get(type);

        if (supplier == null) {
            throw new IllegalArgumentException("Unknown action type: " + type);
        }

        return supplier.get();
    }

    @Override
    public List<String> getRegisteredActions() {
        return List.copyOf(registry.keySet());
    }
}
