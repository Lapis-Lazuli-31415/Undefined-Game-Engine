package entity.scripting.expression;

import entity.scripting.environment.Environment;

public class NotExpression implements BooleanExpression {
    private BooleanExpression expression;

    public NotExpression(BooleanExpression expression) {
        this.expression = expression;
    }

    public void setExpression(BooleanExpression expression) {
        this.expression = expression;
    }

    public BooleanExpression getExpression() {
        return expression;
    }

    @Override
    public Boolean evaluate(Environment globalEnvironment, Environment localEnvironment) throws Exception {
        boolean value = expression.evaluate(globalEnvironment, localEnvironment);

        return !value;
    }
}
