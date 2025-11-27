package use_case.variable.factory;

import entity.scripting.expression.variable.NumericVariable;
import entity.scripting.expression.variable.Variable;

public class NumericVariableFactory implements VariableFactory {

    private static final String TYPE_NAME = "Numeric";

    @Override
    public String getTypename(){
        return TYPE_NAME;
    }

    @Override
    public Variable<?> createVariable(String name, boolean isGlobal){
        return new NumericVariable(name, isGlobal);
    }

    @Override
    public Object parseValue(String rawValue){
        double parsed = Double.parseDouble(rawValue);   // the interactor will catch NumberFormatException error
        return parsed;
    }

    @Override
    public String formatValue(Object value){
        return value.toString();
    }

}
