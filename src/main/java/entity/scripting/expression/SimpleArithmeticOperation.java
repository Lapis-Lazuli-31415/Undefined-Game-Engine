package entity.scripting.expression;

import entity.scripting.environment.Environment;
import entity.scripting.error.NumericComparisonException;
import entity.scripting.error.SimpleArithmeticException;

public class SimpleArithmeticOperation extends NumericExpression{
    private NumericExpression left;
    private String operator;
    private NumericExpression right;

    public SimpleArithmeticOperation(NumericExpression left, String operator, NumericExpression right){
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Double evaluate(Environment globalEnvironment, Environment localEnvironment) throws Exception {
        double leftValue = left.evaluate(globalEnvironment, localEnvironment);
        double rightValue = right.evaluate(globalEnvironment, localEnvironment);

        switch (operator){
            case "+":
                return leftValue + rightValue;
            case "-":
                return leftValue - rightValue;
            case "*":
                return leftValue * rightValue;
            case "/":
                return leftValue / rightValue;
            default:
                throw new SimpleArithmeticException("Invalid operator: " + operator);
        }
    }
}
