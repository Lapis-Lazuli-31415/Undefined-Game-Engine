package use_case.variable.get;

import java.util.List;

public class GetAllVariablesOutputData {
    private final List<VariableData> variables;

    public GetAllVariablesOutputData(List<VariableData> variables) {
        this.variables = variables;
    }

    public List<VariableData> getVariables() {
        return variables;
    }


    public static class VariableData {
        private final String name;
        private final String type;
        private final String value;
        private final boolean isGlobal;

        public VariableData(String name, String type, String value, boolean isGlobal) {
            this.name = name;
            this.type = type;
            this.value = value;
            this.isGlobal = isGlobal;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        public boolean isGlobal() {
            return isGlobal;
        }
    }
}