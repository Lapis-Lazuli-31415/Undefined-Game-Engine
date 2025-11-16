package entity.scripting.action;

import entity.scripting.environment.Assign;
import entity.scripting.environment.Environment;
import entity.scripting.expression.NumericExpression;
import entity.scripting.expression.variable.NumericVariable;

public class NumericVariableAssignmentAction extends Action{
    private NumericVariable variable;
    private NumericExpression expression;

    public NumericVariableAssignmentAction(NumericVariable variable, NumericExpression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public void execute(Environment globalEnvironment, Environment localEnvironment) throws Exception {
        double value = expression.evaluate(globalEnvironment, localEnvironment);

        if (variable.isGlobal()) {
            Assign.assign(globalEnvironment, variable, value);
        } else {
            Assign.assign(localEnvironment, variable, value);
        }
    }

}
