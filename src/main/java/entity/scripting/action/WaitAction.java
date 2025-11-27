package entity.scripting.action;

import entity.scripting.environment.Environment;
import entity.scripting.error.EnvironmentException;
import entity.scripting.error.WaitActionException;
import entity.scripting.expression.NumericExpression;
import entity.scripting.expression.value.NumericValue;

public class WaitAction extends Action{
    public static final String ACTION_TYPE = "Wait";

    private NumericExpression secondsExpression;

    public WaitAction(NumericExpression secondsExpression) {
        this.secondsExpression = secondsExpression;
    }

    public WaitAction() {
        final NumericExpression DEFAULT_SECOND = new NumericValue(0);
        new WaitAction(DEFAULT_SECOND);
    }

    public static String getEventType() {
        return ACTION_TYPE;
    }

    @Override
    public void execute(Environment globalEnvironment, Environment localEnvironment) throws Exception {
        double seconds = secondsExpression.evaluate(globalEnvironment, localEnvironment);

        if (seconds < 0) {
            throw new WaitActionException("Wait time cannot be negative");
        }

        long milliseconds = (long) seconds * 1000;

        Thread.sleep(milliseconds);
    }
}
