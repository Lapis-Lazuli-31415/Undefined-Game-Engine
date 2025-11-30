package use_case.variable.delete;

public class DeleteVariableOutputData {
    private final String name;
    private final boolean isGlobal;
    private final String type;

    public DeleteVariableOutputData(String name, boolean isGlobal, String type) {
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
