package entity.scripting.action;

import entity.Scene;
import entity.scripting.environment.Assign;
import entity.scripting.environment.Environment;
import entity.scripting.error.ParseSyntaxException;
import entity.scripting.expression.BooleanExpression;
import entity.scripting.expression.Expression;
import entity.scripting.expression.ExpressionFactory;
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

    public BooleanVariableAssignmentAction() {
        this(new BooleanVariable("_", false), new BooleanValue(false));
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
    public void execute(Environment globalEnvironment, Environment localEnvironment, Scene scene) throws Exception {
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

    @Override
    public Action parse(String string) throws ParseSyntaxException {
        String[] parts = string.split(";");
        final int requiredLength = 2;

        if (parts.length != requiredLength) {
            throw new ParseSyntaxException("Invalid Syntax (BooleanVariableAssignment): expected " + requiredLength +
                    " parameters but got " + parts.length);
        }

        String variable = parts[0].trim();
        String expression = parts[1].trim();

        Expression<?> variableResult = ExpressionFactory.parse(variable);
        Expression<?> expressionResult = ExpressionFactory.parse(expression);

        if (!(variableResult instanceof BooleanVariable)) {
            throw new ParseSyntaxException("Invalid Syntax: " + variable + " does not evaluate to a BooleanVariable");
        } else if (!(expressionResult instanceof BooleanExpression)) {
            throw new ParseSyntaxException("Invalid Syntax: " + expression + " does not evaluate to a BooleanExpression");
        } else {
            this.variable = (BooleanVariable) variableResult;
            this.expression = (BooleanExpression) expressionResult;
        }
        return null;
    }

    @Override
    public String format() {
        return variable.format() + "; " + expression.format();
    }
}
