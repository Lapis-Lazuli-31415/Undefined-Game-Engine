package use_case.variable;


public final class UpdateVariableOutputData {
    private final String name;
    private final String value;
    private final boolean isGlobal;
    private final String type;

    public UpdateVariableOutputData(String name, String value, boolean isGlobal, String type) {
        this.name = name;
        this.value = value;
        this.isGlobal = isGlobal;
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public String getValue() {
        return value;
    }
    public boolean isGlobal() {
        return isGlobal;
    }
    public String getType() {
        return type;
    }


}
