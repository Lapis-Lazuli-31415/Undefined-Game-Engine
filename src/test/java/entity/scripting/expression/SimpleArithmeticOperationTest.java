package entity.scripting.expression;

import entity.scripting.environment.Assign;
import entity.scripting.environment.Environment;
import entity.scripting.error.SimpleArithmeticException;
import entity.scripting.expression.value.NumericValue;
import entity.scripting.expression.variable.NumericVariable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleArithmeticOperationTest {

    Environment globalEnvironment = new Environment();
    Environment localEnvironment = new Environment();

    @BeforeEach
    void setUp() throws Exception {
        globalEnvironment = new Environment();
        localEnvironment = new Environment();

        // Set up some numeric variables in the environment
        Assign.assign(globalEnvironment, new NumericVariable("x", true), 10.0);
        Assign.assign(localEnvironment, new NumericVariable("y", false), 5.0);
    }

    @Test
    void evaluateAddition_TwoVariables() throws Exception {
        // Use NumericVariable to read values from environment
        NumericVariable varX = new NumericVariable("x", true);   // global
        NumericVariable varY = new NumericVariable("y", false);  // local

        SimpleArithmeticOperation add = new SimpleArithmeticOperation(varX, "+", varY);

        Double result = add.evaluate(globalEnvironment, localEnvironment);

        assertEquals(15.0, result);
    }

    @Test
    void evaluateMultiplication_LiteralAndVariable() throws Exception {
        NumericVariable varX = new NumericVariable("x", true); // 10
        NumericValue literal = new NumericValue(3);            // literal value

        SimpleArithmeticOperation multiply = new SimpleArithmeticOperation(varX, "*", literal);

        Double result = multiply.evaluate(globalEnvironment, localEnvironment);

        assertEquals(30.0, result);
    }

    @Test
    void evaluateSubtractionAndDivision() throws Exception {
        NumericVariable varX = new NumericVariable("x", true); // 10
        NumericVariable varY = new NumericVariable("y", false); // 5

        SimpleArithmeticOperation subtract = new SimpleArithmeticOperation(varX, "-", varY);
        SimpleArithmeticOperation divide = new SimpleArithmeticOperation(varX, "/", varY);

        assertEquals(5.0, subtract.evaluate(globalEnvironment, localEnvironment));
        assertEquals(2.0, divide.evaluate(globalEnvironment, localEnvironment));
    }

    @Test
    void evaluateInvalidOperator() {
        NumericVariable varX = new NumericVariable("x", true);
        NumericVariable varY = new NumericVariable("y", false);

        SimpleArithmeticOperation invalid = new SimpleArithmeticOperation(varX, "%", varY);

        assertThrows(SimpleArithmeticException.class, () -> invalid.evaluate(globalEnvironment, localEnvironment));
    }


}
