package use_case.variable;

public class DeleteVariableInputData {
    private final String name;
    private final boolean isGlobal;
    private final String type;
    public DeleteVariableInputData(String name, boolean isGlobal, String type) {
        this.name = name;
        this.isGlobal = isGlobal;
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public boolean isGlobal() {
        return isGlobal;
    }
    public String getType() {
        return type;
    }
}
