package entity.scripting.expression;

import entity.scripting.environment.Assign;
import entity.scripting.environment.Environment;
import entity.scripting.expression.value.BooleanValue;
import entity.scripting.expression.variable.BooleanVariable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AndExpressionTest {

    Environment globalEnvironment;
    Environment localEnvironment;

    @BeforeEach
    void setUp() throws Exception {
        globalEnvironment = new Environment();
        localEnvironment = new Environment();

        Assign.assign(globalEnvironment, new BooleanVariable("check1", true), true);
        Assign.assign(localEnvironment, new BooleanVariable("check2", false), false);
    }

    @Test
    void evaluateTrueAndTrue() throws Exception {
        BooleanVariable var1 = new BooleanVariable("check1", true);   // global: true
        BooleanValue valTrue = new BooleanValue(true);              // literal true

        AndExpression andExp = new AndExpression(valTrue, var1);

        assertTrue(andExp.evaluate(globalEnvironment, localEnvironment));
    }

    @Test
    void evaluateTrueAndFalse() throws Exception {
        BooleanVariable var2 = new BooleanVariable("check2", false); // local: false
        BooleanValue valTrue = new BooleanValue(true);

        AndExpression andExp = new AndExpression(valTrue, var2);

        assertFalse(andExp.evaluate(globalEnvironment, localEnvironment));
    }

    @Test
    void evaluateFalseAndFalse() throws Exception {
        BooleanVariable var2 = new BooleanVariable("check2", false);
        BooleanValue valFalse = new BooleanValue(false);

        AndExpression andExp = new AndExpression(valFalse, var2);

        assertFalse(andExp.evaluate(globalEnvironment, localEnvironment));
    }

    @Test
    void evaluateNestedAnd() throws Exception {
        BooleanVariable var1 = new BooleanVariable("check1", true);     // global: true
        BooleanVariable var2 = new BooleanVariable("check2", false);    // local: false
        BooleanValue valTrue = new BooleanValue(true);

        // (check1 AND check2) AND true
        AndExpression nested = new AndExpression(
                new AndExpression(var1, var2),
                valTrue
        );

        assertFalse(nested.evaluate(globalEnvironment, localEnvironment));
    }
}