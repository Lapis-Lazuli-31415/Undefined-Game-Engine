package entity.scripting.environment;

import entity.scripting.error.EnvironmentException;
import entity.scripting.expression.variable.BooleanVariable;
import entity.scripting.expression.variable.NumericVariable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnassignTest {
    @Test
    void unassignExistingVariable() throws Exception {
        Environment env = new Environment();
        NumericVariable var = new NumericVariable("x", true);

        // assign first
        Assign.assign(env, var, 5.0);

        // now unassign
        Unassign.unassign(env, var);

        assertThrows(EnvironmentException.class, () -> env.get(var));
    }

    @Test
    void unassignFromEmptyEnvironment() {
        Environment env = new Environment();
        BooleanVariable var = new BooleanVariable("check", true);

        // category "Boolean" doesn't exist
        assertThrows(EnvironmentException.class, () -> Unassign.unassign(env, var));
    }
}
