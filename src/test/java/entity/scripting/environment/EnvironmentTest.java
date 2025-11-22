package entity.scripting.environment;

import entity.scripting.expression.variable.NumericVariable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnvironmentTest {
    @Test
    void containExistingVariable() throws Exception {
        Environment env = new Environment();
        NumericVariable var = new NumericVariable("x", true);

        Assign.assign(env, var, 42.0);

        assertTrue(env.contains(var));
    }

    @Test
    void containNonExistingVariable() throws Exception {
        Environment env = new Environment();
        NumericVariable var = new NumericVariable("x", true);

        assertFalse(env.contains(var));
    }
}
