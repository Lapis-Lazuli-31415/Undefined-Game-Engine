package entity.scripting.action;

import entity.scripting.environment.Environment;
import entity.scripting.error.EnvironmentException;
import entity.scripting.error.WaitActionException;
import entity.scripting.expression.NumericExpression;

public class WaitAction extends Action{
    private NumericExpression secondsExpression;

    public WaitAction(NumericExpression secondsExpression) {
        this.secondsExpression = secondsExpression;
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
