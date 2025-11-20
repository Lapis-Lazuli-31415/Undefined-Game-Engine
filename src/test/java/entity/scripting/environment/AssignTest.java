package entity.scripting.environment;

import entity.scripting.error.EnvironmentException;
import entity.scripting.expression.variable.BooleanVariable;
import entity.scripting.expression.variable.NumericVariable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssignTest {
    @Test
    void assignNumericVariable() throws Exception {
        Environment env = new Environment();
        NumericVariable var = new NumericVariable("x", true);

        Assign.assign(env, var, 42.0);

        assertEquals(42.0, env.get(var));
    }

    @Test
    void assignBooleanVariable() throws Exception {
        Environment env = new Environment();
        BooleanVariable var = new BooleanVariable("check", true);

        Assign.assign(env, var, true);

        assertTrue(env.get(var));
    }

    @Test
    void reassignVariable() throws Exception {
        Environment env = new Environment();
        NumericVariable var = new NumericVariable("x", true);

        Assign.assign(env, var, 42.0);
        Assign.assign(env, var, 10.0); // Reassign

        assertEquals(10.0, env.get(var));
    }
}
