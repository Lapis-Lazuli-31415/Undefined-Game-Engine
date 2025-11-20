package entity.scripting.condition;

import entity.scripting.environment.Environment;
import entity.scripting.error.NumericComparisonException;
import entity.scripting.expression.NumericExpression;

public class NumericComparisonCondition extends Condition{
    private NumericExpression left;
    private String comparator;
    private NumericExpression right;

    public NumericComparisonCondition(NumericExpression left, String comparator, NumericExpression right){
        this.left = left;
        this.comparator = comparator;
        this.right = right;
    }

    public void setLeft(NumericExpression left) {
        this.left = left;
    }

    public void setComparator(String comparator) {
        this.comparator = comparator;
    }

    public void setRight(NumericExpression right) {
        this.right = right;
    }

    @Override
    public boolean evaluate(Environment globalEnvironment, Environment localEnvironment) throws Exception {
        double leftValue = left.evaluate(globalEnvironment, localEnvironment);
        double rightValue = right.evaluate(globalEnvironment, localEnvironment);

        return switch (comparator) {
            case ">" -> leftValue > rightValue;
            case "=" -> leftValue == rightValue;
            case "<" -> leftValue < rightValue;
            case ">=" -> leftValue >= rightValue;
            case "<=" -> leftValue <= rightValue;
            case "!=" -> leftValue != rightValue;
            default -> throw new NumericComparisonException("Invalid comparator: " + comparator);
        };
    }

}
