package entity.scripting.condition;

import entity.scripting.environment.Environment;

public class EmptyCondition extends Condition{
    @Override
    public boolean evaluate(Environment globalEnvironment, Environment localEnvironment) {
        return true;
    }
}
