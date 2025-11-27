package interface_adapter.variable;

import java.util.ArrayList;
import java.util.List;

public class VariableState {

    private List<VariableRow> variables = new ArrayList<>();
    private String errorMessage;

    public static class VariableRow {

        private String name;
        private String type;
        private boolean global;
        private String value;

        public VariableRow(String name, String type, boolean global, String value) {
            this.name = name;
            this.type = type;
            this.global = global;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public boolean isGlobal() {
            return global;
        }

        public String getValue() {
            return value;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setGlobal(boolean global) {
            this.global = global;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


    public List<VariableRow> getVariables() {
        return variables;
    }

    public void setVariables(List<VariableRow> variables) {
        this.variables = variables;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
