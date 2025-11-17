package entity.scripting.action;

import entity.scripting.environment.Environment;

public class EmptyAction extends Action{
    @Override
    public void execute(Environment globalEnvironment, Environment localEnvironment){}
}
