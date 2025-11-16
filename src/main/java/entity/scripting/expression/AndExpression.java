package entity.scripting.expression;

import entity.scripting.environment.Environment;
import entity.scripting.expression.variable.BooleanVariable;

public class AndExpression extends BooleanExpression {
    private BooleanExpression left;
    private BooleanExpression right;

    public AndExpression(BooleanExpression left, BooleanExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Boolean evaluate(Environment globalEnvironment, Environment localEnvironment) throws Exception {
        boolean leftValue = left.evaluate(globalEnvironment, localEnvironment);
        boolean rightValue = right.evaluate(globalEnvironment, localEnvironment);

        return leftValue && rightValue;
    }
}
