package entity.scripting.condition;

import entity.scripting.environment.Environment;
import entity.scripting.error.NumericComparisonException;
import entity.scripting.error.ParseSyntaxException;
import entity.scripting.expression.Expression;
import entity.scripting.expression.ExpressionFactory;
import entity.scripting.expression.NumericExpression;
import entity.scripting.expression.value.NumericValue;

import java.util.Set;

public class NumericComparisonCondition extends Condition{
    public static final String CONDITION_TYPE = "Numeric Comparison";

    private NumericExpression left;
    private String comparator;
    private NumericExpression right;

    private static final Set<String> VALID_COMPARATORS = Set.of(">", "=", "<", ">=", "<=", "!=");

    public NumericComparisonCondition(NumericExpression left, String comparator, NumericExpression right){
        this.left = left;
        this.comparator = comparator;
        this.right = right;
    }

    public NumericComparisonCondition(){
        this(new NumericValue(0), "=", new NumericValue(0));
    }

    public NumericExpression getLeft() {
        return left;
    }

    public String getComparator() {
        return comparator;
    }

    public NumericExpression getRight() {
        return right;
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
    public String getConditionType() {
        return CONDITION_TYPE;
    }

    @Override
    public boolean evaluate(Environment globalEnvironment, Environment localEnvironment) throws Exception {
        double leftValue = left.evaluate(globalEnvironment, localEnvironment);
        double rightValue = right.evaluate(globalEnvironment, localEnvironment);

        System.out.println("Comparing " + leftValue + " " + comparator + " " + rightValue);
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

    @Override
    public Condition parse(String string) throws ParseSyntaxException {
        String[] parts = string.split(";");
        final int requiredLength = 3;

        if (parts.length != requiredLength) {
            throw new ParseSyntaxException("Invalid Syntax (NumericComparison): expected " + requiredLength +
                    " parameters but got " + parts.length);
        }

        String left = parts[0].trim();
        String comparator = parts[1].trim();
        String right = parts[2].trim();

        Expression<?> leftResult = ExpressionFactory.parse(left);
        Expression<?> rightResult = ExpressionFactory.parse(right);

        if (!(leftResult instanceof NumericExpression)) {
            throw new ParseSyntaxException("Invalid Syntax: " + left + " does not evaluate to a NumericExpression");
        } else if (!isValidComparator(comparator)) {
            throw new ParseSyntaxException("Invalid Syntax: " +
                    comparator + " does not belong to \">\", \"=\", \"<\", \">=\", \"<=\", \"!=\"");
        } else if (!(rightResult instanceof NumericExpression)) {
            throw new ParseSyntaxException("Invalid Syntax: " + right + " does not evaluate to a NumericExpression");
        } else {
            this.left = (NumericExpression) leftResult;
            this.comparator = comparator;
            this.right = (NumericExpression) rightResult;
        }
        return null;
    }

    private static boolean isValidComparator(String comparator) {
        return VALID_COMPARATORS.contains(comparator);
    }

    @Override
    public String format() {
        return left.format() + "; " + comparator + "; " + right.format();
    }

}
