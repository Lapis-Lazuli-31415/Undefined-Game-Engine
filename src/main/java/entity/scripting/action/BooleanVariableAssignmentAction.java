package entity.scripting.action;

import entity.scripting.environment.Assign;
import entity.scripting.environment.Environment;
import entity.scripting.expression.BooleanExpression;
import entity.scripting.expression.variable.BooleanVariable;

public class BooleanVariableAssignmentAction extends Action {
    private BooleanVariable variable;
    private BooleanExpression expression;

    public BooleanVariableAssignmentAction(BooleanVariable variable, BooleanExpression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public void execute(Environment globalEnvironment, Environment localEnvironment) throws Exception {
        boolean value = expression.evaluate(globalEnvironment, localEnvironment);

        if (variable.isGlobal()) {
            Assign.assign(globalEnvironment, variable, value);
        } else {
            Assign.assign(localEnvironment, variable, value);
        }
    }
}
