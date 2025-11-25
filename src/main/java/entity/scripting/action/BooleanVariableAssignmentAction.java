package entity.scripting.action;

import entity.scripting.environment.Assign;
import entity.scripting.environment.Environment;
import entity.scripting.expression.BooleanExpression;
import entity.scripting.expression.value.BooleanValue;
import entity.scripting.expression.variable.BooleanVariable;

public class BooleanVariableAssignmentAction extends Action {
    public static final String ACTION_TYPE = "Boolean Variable Assignment";

    private BooleanVariable variable;
    private BooleanExpression expression;

    public BooleanVariableAssignmentAction(BooleanVariable variable, BooleanExpression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    public BooleanVariableAssignmentAction(BooleanExpression expression) {
        this.expression = expression;
    }

    public BooleanVariableAssignmentAction() {
        final BooleanExpression DEFAULT_EXPRESSION = new BooleanValue(false);
        this.expression = DEFAULT_EXPRESSION;
    }

    public static String getEventType() {
        return ACTION_TYPE;
    }

    public void setBooleanVariable(BooleanVariable variable) {
        this.variable = variable;
    }

    public void setBooleanExpression(BooleanExpression expression) {
        this.expression = expression;
    }

    public BooleanVariable getBooleanVariable() {
        return variable;
    }

    public BooleanExpression getBooleanExpression() {
        return expression;
    }

    @Override
    public void execute(Environment globalEnvironment, Environment localEnvironment) throws Exception {
        if (expression == null) {
            return;
        }

        boolean value = expression.evaluate(globalEnvironment, localEnvironment);

        if (variable.isGlobal()) {
            Assign.assign(globalEnvironment, variable, value);
        } else {
            Assign.assign(localEnvironment, variable, value);
        }
    }
}
