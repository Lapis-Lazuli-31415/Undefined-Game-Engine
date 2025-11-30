package entity.scripting.condition;

import entity.scripting.environment.Environment;
import entity.scripting.error.ParseSyntaxException;
import entity.scripting.expression.BooleanExpression;
import entity.scripting.expression.Expression;
import entity.scripting.expression.ExpressionFactory;
import entity.scripting.expression.value.BooleanValue;

public class BooleanComparisonCondition extends Condition {
    public static final String EVENT_TYPE = "Boolean Comparison";
    private BooleanExpression left;
    private BooleanExpression right;

    public BooleanComparisonCondition(BooleanExpression left, BooleanExpression right) {
        this.left = left;
        this.right = right;
    }

    public BooleanComparisonCondition(){
        this(new BooleanValue(false), new BooleanValue(false));
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

    public static String getEventType() {
        return EVENT_TYPE;
    }

    @Override
    public boolean evaluate(Environment globalEnvironment, Environment localEnvironment) throws Exception {
        boolean leftValue = left.evaluate(globalEnvironment, localEnvironment);
        boolean rightValue = right.evaluate(globalEnvironment, localEnvironment);

        return leftValue == rightValue;
    }

    @Override
    public Condition parse(String string) throws ParseSyntaxException {
        String[] parts = string.split(";");
        final int requiredLength = 2;

        if (parts.length != requiredLength) {
            throw new ParseSyntaxException("Invalid Syntax (BooleanComparison): expected " + requiredLength +
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
            this.left = (BooleanExpression) leftResult;
            this.right = (BooleanExpression) rightResult;
        }
        return null;
    }

    @Override
    public String format() {
        return left.format() + "; " + right.format();
    }
}
