package entity.scripting.expression;

import entity.scripting.environment.Environment;

public class NotExpression extends BooleanExpression {
    private BooleanExpression expression;

    public NotExpression(BooleanExpression expression) {
        this.expression = expression;
    }

    @Override
    public Boolean evaluate(Environment globalEnvironment, Environment localEnvironment) throws Exception {
        boolean value = expression.evaluate(globalEnvironment, localEnvironment);

        return !value;
    }
}
