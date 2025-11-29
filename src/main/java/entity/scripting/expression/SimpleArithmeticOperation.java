package entity.scripting.expression;

import entity.scripting.environment.Environment;
import entity.scripting.error.ParseSyntaxException;
import entity.scripting.error.SimpleArithmeticException;
import entity.scripting.expression.variable.BooleanVariable;

import java.util.Set;

public class SimpleArithmeticOperation implements NumericExpression{
    private NumericExpression left;
    private String operator;
    private NumericExpression right;
    public static final String KEY_WORD = "arithmetic";
    private static final Set<String> VALID_OPERATORS = Set.of("+", "-", "*", "/");

    public SimpleArithmeticOperation(NumericExpression left, String operator, NumericExpression right){
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public void setLeft(NumericExpression left) {
        this.left = left;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setRight(NumericExpression right) {
        this.right = right;
    }

    public NumericExpression getLeft() {
        return left;
    }
    public String getOperator() {
        return operator;
    }

    public NumericExpression getRight() {
        return right;
    }

    @Override
    public Double evaluate(Environment globalEnvironment, Environment localEnvironment) throws Exception {
        double leftValue = left.evaluate(globalEnvironment, localEnvironment);
        double rightValue = right.evaluate(globalEnvironment, localEnvironment);

        return switch (operator) {
            case "+" -> leftValue + rightValue;
            case "-" -> leftValue - rightValue;
            case "*" -> leftValue * rightValue;
            case "/" -> {
                if (rightValue == 0) {
                    throw new SimpleArithmeticException("Invalid operation: division by 0");
                }
                yield leftValue / rightValue;
            }
            default -> throw new SimpleArithmeticException("Invalid operator: " + operator);
        };
    }

    public static SimpleArithmeticOperation parse(String string) throws ParseSyntaxException {
        String[] parts = ExpressionFactory.split(string).toArray(new String[0]);
        final int requiredLength = 3;

        if (parts.length != requiredLength) {
            throw new ParseSyntaxException("Invalid Syntax (SimpleArithmeticOperation): expected " + requiredLength +
                    " parameters but got " + parts.length);
        }

        String left = parts[0].trim();
        String operator = parts[1].trim();
        String right = parts[2].trim();

        Expression<?> leftResult = ExpressionFactory.parse(left);
        Expression<?> rightResult = ExpressionFactory.parse(right);

        if (!(leftResult instanceof NumericExpression)) {
            throw new ParseSyntaxException("Invalid Syntax: " + left + " does not evaluate to a BooleanExpression");
        } else if (!isValidOperator(operator)) {
            throw new ParseSyntaxException("Invalid Syntax: " +
                    operator + " does not belong to \"+\", \"-\", \"*\", \"/\"");
        } else if (!(rightResult instanceof NumericExpression)) {
            throw new ParseSyntaxException("Invalid Syntax: " + right + " does not evaluate to a BooleanExpression");
        } else {
            return new SimpleArithmeticOperation((NumericExpression) leftResult,
                    operator, (NumericExpression) rightResult);
        }
    }

    private static boolean isValidOperator(String operator) {
        return VALID_OPERATORS.contains(operator);
    }

    @Override
    public String format() {
        return KEY_WORD + ":(" + left.format() + ", " + operator + ", " + right.format() + ")";
    }
}
