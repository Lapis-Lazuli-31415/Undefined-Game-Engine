package entity.scripting.expression.value;

import entity.scripting.environment.Environment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NumericValueTest {

    Environment global = new Environment();
    Environment local  = new Environment();

    @Test
    void evaluatePositiveNumber() {
        NumericValue val = new NumericValue(42.5);

        Double result = val.evaluate(global, local);

        assertEquals(42.5, result);
    }

    @Test
    void evaluateNegativeNumber() {
        NumericValue val = new NumericValue(-10.75);

        Double result = val.evaluate(global, local);

        assertEquals(-10.75, result);
    }

    @Test
    void evaluateZero() {
        NumericValue val = new NumericValue(0);

        Double result = val.evaluate(global, local);

        assertEquals(0.0, result);
    }
}
