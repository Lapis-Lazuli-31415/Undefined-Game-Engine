package entity.scripting.expression;

import entity.scripting.environment.Environment;
import entity.scripting.error.ParseSyntaxException;
import entity.scripting.expression.variable.BooleanVariable;

public class NotExpression implements BooleanExpression {
    private BooleanExpression expression;
    public static final String KEY_WORD = "not";

    public NotExpression(BooleanExpression expression) {
        this.expression = expression;
    }

    public void setExpression(BooleanExpression expression) {
        this.expression = expression;
    }

    public BooleanExpression getExpression() {
        return expression;
    }

    @Override
    public Boolean evaluate(Environment globalEnvironment, Environment localEnvironment) throws Exception {
        boolean value = expression.evaluate(globalEnvironment, localEnvironment);

        return !value;
    }

    public static NotExpression parse(String string) throws ParseSyntaxException {
        String[] parts = ExpressionFactory.split(string).toArray(new String[0]);
        final int requiredLength = 1;

        if (parts.length != requiredLength) {
            throw new ParseSyntaxException("Invalid Syntax (NotExpression): expected " + requiredLength +
                    " parameters but got " + parts.length);
        }

        String expression = parts[0].trim();

        Expression<?> expressionResult = ExpressionFactory.parse(expression);

        if (!(expressionResult instanceof BooleanExpression)) {
            throw new ParseSyntaxException("Invalid Syntax: " + expression + " does not evaluate to a BooleanExpression");
        } else {
            return new NotExpression((BooleanExpression) expressionResult);
        }
    }

    @Override
    public String format() {
        return KEY_WORD + ":(" + expression.format() + ")";
    }
}
