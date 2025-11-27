package use_case.variable.factory;

import entity.scripting.expression.variable.BooleanVariable;
import entity.scripting.expression.variable.Variable;

public class BooleanVariableFactory implements VariableFactory{

    private static final String TYPE_NAME = "Boolean";

    @Override
    public String getTypename(){
        return TYPE_NAME;
    }

    @Override
    public Variable<?> createVariable(String name, boolean isGlobal){
        return new BooleanVariable(name, isGlobal);
    }

    @Override
    public Object parseValue(String rawValue){
        String normalized = rawValue.toLowerCase();

        if ("true".equals(normalized)){
            return Boolean.TRUE;
        } else if("false".equals(normalized)){
            return Boolean.FALSE;
        }

        throw new IllegalArgumentException("Invalid boolean value: " + rawValue);
    }

    @Override
    public String formatValue(Object value){
        return value.toString();
    }

}
