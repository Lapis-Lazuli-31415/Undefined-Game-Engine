package use_case.variable;

import entity.scripting.environment.Environment;
import entity.scripting.expression.variable.NumericVariable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.variable.update.UpdateVariableInputData;
import use_case.variable.update.UpdateVariableInteractor;
import use_case.variable.update.UpdateVariableOutputBoundary;
import use_case.variable.update.UpdateVariableOutputData;

import static org.junit.jupiter.api.Assertions.*;



class UpdateVariableInteractorTest {

    private Environment globalEnv;
    private Environment localEnv;
    private TestUpdatePresenter presenter;
    private UpdateVariableInteractor interactor;

    @BeforeEach
    void setUp() {
        globalEnv = new Environment();
        localEnv = new Environment();
        presenter = new TestUpdatePresenter();
        interactor = new UpdateVariableInteractor(globalEnv, localEnv, presenter);
    }

    @Test
    void testUpdateNumericVariable_NewVariable() {
        UpdateVariableInputData input = new UpdateVariableInputData(
                "score", "100", false, "Numeric"
        );

        interactor.execute(input);

        assertTrue(presenter.isSuccessCalled, "Success view should be called");
        assertFalse(presenter.isFailureCalled, "Failure view should not be called");
        assertEquals("score", presenter.outputData.getName());
        assertEquals("Numeric", presenter.outputData.getType());
        assertEquals("100.0", presenter.outputData.getValue());
        assertFalse(presenter.outputData.isGlobal());
    }

    @Test
    void testUpdateNumericVariable_UpdateExisting() throws Exception {
        UpdateVariableInputData input1 = new UpdateVariableInputData(
                "health", "100", false, "Numeric"
        );
        interactor.execute(input1);

        presenter.reset();

        UpdateVariableInputData input2 = new UpdateVariableInputData(
                "health", "50.5", false, "Numeric"
        );
        interactor.execute(input2);

        assertTrue(presenter.isSuccessCalled);
        assertEquals("health", presenter.outputData.getName());
        assertEquals("50.5", presenter.outputData.getValue());

        NumericVariable var = new NumericVariable("health", false);
        assertEquals(50.5, localEnv.get(var));
    }

    @Test
    void testUpdateBooleanVariable_True() {
        UpdateVariableInputData input = new UpdateVariableInputData(
                "isAlive", "true", true, "Boolean"
        );

        interactor.execute(input);

        assertTrue(presenter.isSuccessCalled);
        assertEquals("isAlive", presenter.outputData.getName());
        assertEquals("Boolean", presenter.outputData.getType());
        assertEquals("true", presenter.outputData.getValue());
        assertTrue(presenter.outputData.isGlobal());
    }

    @Test
    void testUpdateBooleanVariable_False() {
        UpdateVariableInputData input = new UpdateVariableInputData(
                "gameOver", "false", false, "Boolean"
        );

        interactor.execute(input);

        assertTrue(presenter.isSuccessCalled);
        assertEquals("false", presenter.outputData.getValue());
    }

    @Test
    void testUpdateBooleanVariable_CaseInsensitive() {
        UpdateVariableInputData input1 = new UpdateVariableInputData(
                "flag1", "TRUE", false, "Boolean"
        );
        interactor.execute(input1);
        assertTrue(presenter.isSuccessCalled);
        assertEquals("true", presenter.outputData.getValue());

        presenter.reset();

        UpdateVariableInputData input2 = new UpdateVariableInputData(
                "flag2", "False", false, "Boolean"
        );
        interactor.execute(input2);
        assertTrue(presenter.isSuccessCalled);
        assertEquals("false", presenter.outputData.getValue());
    }

    @Test
    void testUpdateVariable_EmptyName() {
        UpdateVariableInputData input = new UpdateVariableInputData(
                "", "100", false, "Numeric"
        );

        interactor.execute(input);

        assertFalse(presenter.isSuccessCalled);
        assertTrue(presenter.isFailureCalled);
        assertEquals("Variable name cannot be empty.", presenter.errorMessage);
    }

    @Test
    void testUpdateVariable_NullName() {
        UpdateVariableInputData input = new UpdateVariableInputData(
                null, "100", false, "Numeric"
        );

        interactor.execute(input);

        assertTrue(presenter.isFailureCalled);
        assertEquals("Variable name cannot be empty.", presenter.errorMessage);
    }

    @Test
    void testUpdateVariable_EmptyType() {
        UpdateVariableInputData input = new UpdateVariableInputData(
                "score", "100", false, ""
        );

        interactor.execute(input);

        assertTrue(presenter.isFailureCalled);
        assertEquals("Variable type is required.", presenter.errorMessage);
    }

    @Test
    void testUpdateVariable_EmptyValue() {
        UpdateVariableInputData input = new UpdateVariableInputData(
                "score", "", false, "Numeric"
        );

        interactor.execute(input);

        assertTrue(presenter.isFailureCalled);
        assertEquals("Variable value cannot be empty.", presenter.errorMessage);
    }

    @Test
    void testUpdateVariable_InvalidNumericValue() {
        UpdateVariableInputData input = new UpdateVariableInputData(
                "score", "not-a-number", false, "Numeric"
        );

        interactor.execute(input);

        assertTrue(presenter.isFailureCalled);
        assertTrue(presenter.errorMessage.contains("Invalid numeric value"));
    }

    @Test
    void testUpdateExistingNumericVariableAsBooleanShouldFail() throws Exception {
        // First: create a numeric variable
        UpdateVariableInputData numericInput = new UpdateVariableInputData(
                "x", "123", false, "Numeric"
        );
        interactor.execute(numericInput);

        // Ensure first update succeeded
        assertTrue(presenter.isSuccessCalled);
        presenter.reset();

        // Second: try to update x as a Boolean
        UpdateVariableInputData booleanInput = new UpdateVariableInputData(
                "x", "true", false, "Boolean"
        );

        interactor.execute(booleanInput);

        // Expect failure
        assertTrue(presenter.isFailureCalled, "Failure view should be called");
        assertTrue(presenter.errorMessage.contains("Invalid"),
                "Error message should indicate invalid update");

        // Ensure original numeric value is unchanged in the environment
        NumericVariable x = new NumericVariable("x", false);
        assertEquals(123.0, localEnv.get(x));
    }


    @Test
    void testUpdateVariable_InvalidBooleanValue() {
        UpdateVariableInputData input = new UpdateVariableInputData(
                "flag", "maybe", false, "Boolean"
        );

        interactor.execute(input);

        assertTrue(presenter.isFailureCalled);
        assertTrue(presenter.errorMessage.contains("Invalid boolean value"));
    }

    @Test
    void testUpdateVariable_UnsupportedType() {
        UpdateVariableInputData input = new UpdateVariableInputData(
                "data", "value", false, "String"
        );

        interactor.execute(input);

        assertTrue(presenter.isFailureCalled);
        assertTrue(presenter.errorMessage.contains("Unsupported variable type"));
    }

    @Test
    void testUpdateVariable_GlobalVsLocal() throws Exception {
        UpdateVariableInputData globalInput = new UpdateVariableInputData(
                "globalScore", "999", true, "Numeric"
        );
        interactor.execute(globalInput);

        UpdateVariableInputData localInput = new UpdateVariableInputData(
                "globalScore", "111", false, "Numeric"
        );
        interactor.execute(localInput);

        NumericVariable globalVar = new NumericVariable("globalScore", true);
        NumericVariable localVar = new NumericVariable("globalScore", false);

        assertEquals(999.0, globalEnv.get(globalVar));
        assertEquals(111.0, localEnv.get(localVar));
    }

    @Test
    void testUpdateVariable_DecimalNumbers() {
        UpdateVariableInputData input = new UpdateVariableInputData(
                "pi", "3.14159", false, "Numeric"
        );

        interactor.execute(input);

        assertTrue(presenter.isSuccessCalled);
        assertEquals("3.14159", presenter.outputData.getValue());
    }

    @Test
    void testUpdateVariable_NegativeNumbers() {
        UpdateVariableInputData input = new UpdateVariableInputData(
                "temperature", "-40.5", false, "Numeric"
        );

        interactor.execute(input);

        assertTrue(presenter.isSuccessCalled);
        assertEquals("-40.5", presenter.outputData.getValue());
    }

    private static class TestUpdatePresenter implements UpdateVariableOutputBoundary {
        boolean isSuccessCalled = false;
        boolean isFailureCalled = false;
        UpdateVariableOutputData outputData;
        String errorMessage;

        @Override
        public void prepareSuccessView(UpdateVariableOutputData data) {
            isSuccessCalled = true;
            this.outputData = data;
        }

        @Override
        public void prepareFailureView(String errorMessage) {
            isFailureCalled = true;
            this.errorMessage = errorMessage;
        }

        void reset() {
            isSuccessCalled = false;
            isFailureCalled = false;
            outputData = null;
            errorMessage = null;
        }
    }
}