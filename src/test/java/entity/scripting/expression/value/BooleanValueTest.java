package entity.scripting.expression.value;

import entity.scripting.environment.Environment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BooleanValueTest {

    Environment global = new Environment();
    Environment local  = new Environment();

    @Test
    void evaluateTrueValue() {
        BooleanValue val = new BooleanValue(true);

        Boolean result = val.evaluate(global, local);

        assertTrue(result);
    }

    @Test
    void evaluateFalseValue() {
        BooleanValue val = new BooleanValue(false);

        Boolean result = val.evaluate(global, local);

        assertFalse(result);
    }

}
