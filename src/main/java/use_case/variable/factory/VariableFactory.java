package use_case.variable.factory;

import entity.scripting.expression.variable.Variable;

public interface VariableFactory {

    String getTypename();

    Variable<?> createVariable(String name, boolean isGlobal);

    Object parseValue(String rawValue);

    String formatValue(Object value);

    default ValuePromptKind getPromptKind() {
        return ValuePromptKind.TEXT;
    }
}
