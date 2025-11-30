package entity;

import entity.scripting.environment.Environment;

public class GameController {

    final Environment environment;

    public GameController(Environment environment) {
        this.environment = environment;
    }

    // Jackson needs this to read the data
    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment env) {
    }
}
