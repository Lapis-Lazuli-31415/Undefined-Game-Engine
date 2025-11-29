package entity.scripting.action;

import entity.Scene;
import entity.scripting.environment.Assign;
import entity.scripting.environment.Environment;
import entity.scripting.error.ParseSyntaxException;
import entity.scripting.expression.Expression;
import entity.scripting.expression.ExpressionFactory;
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

    public NumericVariableAssignmentAction() {
        this(new NumericVariable("_", false), new NumericValue(0));
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
    public void execute(Environment globalEnvironment, Environment localEnvironment, Scene scene) throws Exception {
        if (expression == null) {
            return;
        }

        double value = expression.evaluate(globalEnvironment, localEnvironment);

        if (variable.isGlobal()) {
            Assign.assign(globalEnvironment, variable, value);
        } else {
            Assign.assign(localEnvironment, variable, value);
        }
    }

    @Override
    public void parse(String string) throws ParseSyntaxException {
        String[] parts = string.split(";");
        final int requiredLength = 2;

        if (parts.length != requiredLength) {
            throw new ParseSyntaxException("Invalid Syntax (NumericVariableAssignment): expected " + requiredLength +
                    " parameters but got " + parts.length);
        }

        String variable = parts[0].trim();
        String expression = parts[1].trim();

        Expression<?> variableResult = ExpressionFactory.parse(variable);
        Expression<?> expressionResult = ExpressionFactory.parse(expression);

        if (!(variableResult instanceof NumericVariable)) {
            throw new ParseSyntaxException("Invalid Syntax: " + variable + " does not evaluate to a NumericVariable");
        } else if (!(expressionResult instanceof NumericExpression)) {
            throw new ParseSyntaxException("Invalid Syntax: " + expression + " does not evaluate to a NumericExpression");
        } else {
            this.variable = (NumericVariable) variableResult;
            this.expression = (NumericExpression) expressionResult;
        }
    }

    @Override
    public String format() {
        return variable.format() + "; " + expression.format();
    }

}
