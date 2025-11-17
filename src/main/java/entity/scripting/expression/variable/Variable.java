package entity.scripting.expression.variable;

public interface Variable<T> {
    public String getName();
    public boolean isGlobal();
    public Class<T> getValueType();
    public String getVariableType();
}
