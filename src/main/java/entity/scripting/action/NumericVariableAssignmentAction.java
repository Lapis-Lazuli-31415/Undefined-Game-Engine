package entity.scripting.action;

import entity.scripting.environment.Assign;
import entity.scripting.environment.Environment;
import entity.scripting.expression.NumericExpression;
import entity.scripting.expression.value.NumericValue;
import entity.scripting.expression.variable.NumericVariable;

public class NumericVariableAssignmentAction extends Action{
    public static final String ACTION_TYPE = "Numeric Variable Assignment";

    private NumericVariable variable;
    private NumericExpression expression;

    public NumericVariableAssignmentAction(NumericVariable variable, NumericExpression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    public NumericVariableAssignmentAction(NumericExpression expression) {
        this.expression = expression;
    }

    public NumericVariableAssignmentAction() {
        final NumericExpression DEFAULT_EXPRESSION = new NumericValue(0);
        new NumericVariableAssignmentAction(DEFAULT_EXPRESSION);
    }

    public static String getEventType() {
        return ACTION_TYPE;
    }

    public void setNumericVariable(NumericVariable variable) {
        this.variable = variable;
    }

    public void setNumericExpression(NumericExpression expression) {
        this.expression = expression;
    }

    public NumericVariable getNumericVariable() {
        return variable;
    }

    public NumericExpression getNumericExpression() {
        return expression;
    }

    @Override
    public void execute(Environment globalEnvironment, Environment localEnvironment) throws Exception {
        if (expression == null) {
            return;
        }

        double value = expression.evaluate(globalEnvironment, localEnvironment);

        if (variable.isGlobal()) {
            Assign.assign(globalEnvironment, variable, value);
        } else {
            Assign.assign(localEnvironment, variable, value);
        }
        System.out.println("Assigned " + value + " to variable " + variable.getName());
    }

}
