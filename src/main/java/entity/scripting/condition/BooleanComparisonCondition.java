package entity.scripting.condition;

import entity.scripting.environment.Environment;
import entity.scripting.expression.BooleanExpression;
import entity.scripting.expression.NumericExpression;
import entity.scripting.expression.value.BooleanValue;
import entity.scripting.expression.value.NumericValue;

public class BooleanComparisonCondition extends Condition {
    public static final String EVENT_TYPE = "Boolean Comparison";
    private BooleanExpression left;
    private BooleanExpression right;

    public BooleanComparisonCondition(BooleanExpression left, BooleanExpression right) {
        this.left = left;
        this.right = right;
    }

    public BooleanComparisonCondition(){
        final BooleanExpression DEFAULT_LEFT = new BooleanValue(false);
        final BooleanExpression DEFAULT_RIGHT = new BooleanValue(false);
        new BooleanComparisonCondition(DEFAULT_LEFT, DEFAULT_RIGHT);
    }

    public void setLeft(BooleanExpression left) {
        this.left = left;
    }

    public void setRight(BooleanExpression right) {
        this.right = right;
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
}
