package entity.scripting.expression;

import entity.scripting.environment.Environment;
import entity.scripting.error.NumericComparisonException;
import entity.scripting.error.SimpleArithmeticException;

public class SimpleArithmeticOperation implements NumericExpression{
    private NumericExpression left;
    private String operator;
    private NumericExpression right;

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
}
