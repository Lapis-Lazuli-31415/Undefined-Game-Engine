package entity.scripting.condition;

import entity.scripting.environment.Environment;
import entity.scripting.error.NumericComparisonException;
import entity.scripting.expression.NumericExpression;
import entity.scripting.expression.value.NumericValue;

public class NumericComparisonCondition extends Condition{
    public static final String EVENT_TYPE = "Numeric Comparison";

    private NumericExpression left;
    private String comparator;
    private NumericExpression right;

    public NumericComparisonCondition(NumericExpression left, String comparator, NumericExpression right){
        this.left = left;
        this.comparator = comparator;
        this.right = right;
    }

    public NumericComparisonCondition(){
        final NumericExpression DEFAULT_LEFT = new NumericValue(0);
        final String DEFAULT_COMPARATOR = "=";
        final NumericExpression DEFAULT_RIGHT = new NumericValue(0);
        new NumericComparisonCondition(DEFAULT_LEFT, DEFAULT_COMPARATOR, DEFAULT_RIGHT);
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

    public static String getEventType() {
        return EVENT_TYPE;
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
