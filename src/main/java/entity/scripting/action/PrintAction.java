package entity.scripting.action;

import entity.scripting.environment.Environment;

/**
 * 简单的测试 Action，打印消息到控制台
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