package use_case.variable.get;


public class GetAllVariablesInputData {
    private final boolean isGlobal;

    public GetAllVariablesInputData(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public boolean isGlobal() {
        return isGlobal;
    }
}