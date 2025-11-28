package entity.scripting.action;

import entity.scripting.environment.Environment;

/**
 */
public class PrintAction extends Action {

    private String message;

    public PrintAction(String message) {
        this.message = message;
    }

    @Override
    public void execute(Environment globalEnvironment, Environment localEnvironment) throws Exception {
        System.out.println(message);
    }
}