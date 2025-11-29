package entity.scripting.action;

import entity.Scene;
import entity.scripting.environment.Environment;
import entity.scripting.error.ParseSyntaxException;
import entity.scripting.error.WaitActionException;
import entity.scripting.expression.Expression;
import entity.scripting.expression.ExpressionFactory;
import entity.scripting.expression.NumericExpression;
import entity.scripting.expression.value.NumericValue;
import entity.scripting.expression.variable.NumericVariable;

public class WaitAction extends Action{
    public static final String ACTION_TYPE = "Wait";

    private NumericExpression secondsExpression;

    public WaitAction(NumericExpression secondsExpression) {
        this.secondsExpression = secondsExpression;
    }

    public WaitAction() {
        this(new NumericValue(0));
    }

    public static String getEventType() {
        return ACTION_TYPE;
    }

    @Override
    public void execute(Environment globalEnvironment, Environment localEnvironment, Scene scene) throws Exception {
        double seconds = secondsExpression.evaluate(globalEnvironment, localEnvironment);

        if (seconds < 0) {
            throw new WaitActionException("Wait time cannot be negative");
        }

        long milliseconds = (long) seconds * 1000;

        Thread.sleep(milliseconds);
    }

    @Override
    public void parse(String string) throws ParseSyntaxException {
        String[] parts = string.split(";");
        final int requiredLength = 1;

        if (parts.length != requiredLength) {
            throw new ParseSyntaxException("Invalid Syntax (Wait): expected " + requiredLength +
                    " parameters but got " + parts.length);
        }

        String seconds = parts[0].trim();

        Expression<?> secondsResult = ExpressionFactory.parse(seconds);

        if (!(secondsResult instanceof NumericVariable)) {
            throw new ParseSyntaxException("Invalid Syntax: " + seconds + " does not evaluate to a NumericVariable");
        } else {
            this.secondsExpression = (NumericVariable) secondsResult;
        }
    }

    @Override
    public String format() {
        return secondsExpression.format();
    }
}
