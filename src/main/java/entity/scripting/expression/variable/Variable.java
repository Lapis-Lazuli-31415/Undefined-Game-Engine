package entity.scripting.expression.variable;

public abstract class Variable<T> {
    protected final String name;
    protected final boolean isGlobal;

    public Variable(String name, boolean isGlobal) {
        this.name = name;
        this.isGlobal = isGlobal;
    }

    public String getName() {
        return name;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public abstract Class<T> getValueType();
    public abstract String getVariableType();
}
