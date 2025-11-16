package entity.scripting.condition;

import entity.scripting.environment.Environment;
import entity.scripting.expression.BooleanExpression;

public class BooleanComparisonCondition extends Condition {
    private BooleanExpression left;
    private BooleanExpression right;

    public BooleanComparisonCondition(BooleanExpression left, BooleanExpression right) {
        this.left = left;
        this.right = right;
    }

    public void setLeft(BooleanExpression left) {
        this.left = left;
    }

    public void setRight(BooleanExpression right) {
        this.right = right;
    }

    @Override
    public boolean evaluate(Environment globalEnvironment, Environment localEnvironment) throws Exception {
        boolean leftValue = left.evaluate(globalEnvironment, localEnvironment);
        boolean rightValue = right.evaluate(globalEnvironment, localEnvironment);

        return leftValue == rightValue;
    }
}
