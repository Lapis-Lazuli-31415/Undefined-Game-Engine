package entity.scripting.expression;

import entity.scripting.environment.Environment;
import entity.scripting.error.ParseSyntaxException;
import entity.scripting.expression.variable.BooleanVariable;
import entity.scripting.expression.variable.NumericVariable;

public class AndExpression implements BooleanExpression {
    private BooleanExpression left;
    private BooleanExpression right;
    public static final String KEY_WORD = "and";

    public AndExpression(BooleanExpression left, BooleanExpression right) {
        this.left = left;
        this.right = right;
    }

    public void setLeft(BooleanExpression left) {
        this.left = left;
    }

    public void setRight(BooleanExpression right) {
        this.right = right;
    }

    public BooleanExpression getLeft() {
        return left;
    }

    public BooleanExpression getRight() {
        return right;
    }

    @Override
    public Boolean evaluate(Environment globalEnvironment, Environment localEnvironment) throws Exception {
        boolean leftValue = left.evaluate(globalEnvironment, localEnvironment);
        boolean rightValue = right.evaluate(globalEnvironment, localEnvironment);

        return leftValue && rightValue;
    }

    public static AndExpression parse(String string) throws ParseSyntaxException {
        String[] parts = ExpressionFactory.split(string).toArray(new String[0]);
        final int requiredLength = 2;

        if (parts.length != requiredLength) {
            throw new ParseSyntaxException("Invalid Syntax (AndExpression): expected " + requiredLength +
                    " parameters but got " + parts.length);
        }

        String left = parts[0].trim();
        String right = parts[1].trim();

        Expression<?> leftResult = ExpressionFactory.parse(left);
        Expression<?> rightResult = ExpressionFactory.parse(right);

        if (!(leftResult instanceof BooleanExpression)) {
            throw new ParseSyntaxException("Invalid Syntax: " + left + " does not evaluate to a BooleanExpression");
        } else if (!(rightResult instanceof BooleanExpression)) {
            throw new ParseSyntaxException("Invalid Syntax: " + right + " does not evaluate to a BooleanExpression");
        } else {
            return new AndExpression((BooleanExpression) leftResult, (BooleanExpression) rightResult);
        }
    }

    @Override
    public String format() {
        return KEY_WORD + ":(" + left.format() + ", " + right.format() + ")";
    }
}
