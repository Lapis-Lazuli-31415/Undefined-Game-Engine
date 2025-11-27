package use_case.variable;

import entity.scripting.environment.Assign;
import entity.scripting.environment.Environment;
import entity.scripting.error.EnvironmentException;
import entity.scripting.expression.variable.NumericVariable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteVariableInteractorTest {

    private static class StubPresenter implements DeleteVariableOutputBoundary {
        DeleteVariableOutputData lastSuccess;
        String lastError;

        @Override
        public void prepareSuccessView(DeleteVariableOutputData data) {
            this.lastSuccess = data;
        }

        @Override
        public void prepareFailureView(String errorMessage) {
            this.lastError = errorMessage;
        }
    }

    @Test
    void deleteGlobalNumericVariable_success() throws EnvironmentException {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        StubPresenter presenter = new StubPresenter();

        NumericVariable var = new NumericVariable("score", true);
        Assign.assign(globalEnv, var, 42.0);

        DeleteVariableInteractor interactor =
                new DeleteVariableInteractor(globalEnv, localEnv, presenter);

        DeleteVariableInputData input =
                new DeleteVariableInputData("score", true, "Numeric");

        interactor.execute(input);

        assertThrows(EnvironmentException.class, () -> globalEnv.get(var));

        assertNotNull(presenter.lastSuccess);
        assertNull(presenter.lastError);
        assertEquals("score", presenter.lastSuccess.getName());
        assertTrue(presenter.lastSuccess.isGlobal());
        assertEquals("Numeric", presenter.lastSuccess.getType());
    }

    @Test
    void deleteLocalBooleanVariable_success() throws EnvironmentException {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        StubPresenter presenter = new StubPresenter();

        var var = new entity.scripting.expression.variable.BooleanVariable("isAlive", false);
        entity.scripting.environment.Assign.assign(localEnv, var, true);

        DeleteVariableInteractor interactor =
                new DeleteVariableInteractor(globalEnv, localEnv, presenter);

        DeleteVariableInputData input =
                new DeleteVariableInputData("isAlive", false, "Boolean");

        interactor.execute(input);

        assertThrows(entity.scripting.error.EnvironmentException.class,
                () -> localEnv.get(var));

        assertNotNull(presenter.lastSuccess);
        assertNull(presenter.lastError);
        assertEquals("isAlive", presenter.lastSuccess.getName());
        assertFalse(presenter.lastSuccess.isGlobal());
        assertEquals("Boolean", presenter.lastSuccess.getType());
    }

    @Test
    void deleteVariable_unsupportedType_failure() {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        StubPresenter presenter = new StubPresenter();

        DeleteVariableInteractor interactor =
                new DeleteVariableInteractor(globalEnv, localEnv, presenter);

        DeleteVariableInputData input =
                new DeleteVariableInputData("x", true, "String"); // not Numeric/Boolean

        interactor.execute(input);

        assertNull(presenter.lastSuccess);
        assertNotNull(presenter.lastError);
        assertTrue(presenter.lastError.contains("Unsupported variable type"));
    }

    @Test
    void deleteVariable_emptyName_failure() {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        StubPresenter presenter = new StubPresenter();

        DeleteVariableInteractor interactor =
                new DeleteVariableInteractor(globalEnv, localEnv, presenter);

        DeleteVariableInputData input =
                new DeleteVariableInputData("   ", true, "Numeric");

        interactor.execute(input);

        assertNull(presenter.lastSuccess);
        assertEquals("Variable name cannot be empty.", presenter.lastError);
    }
    @Test
    void deleteVariable_emptyType_failure() {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        StubPresenter presenter = new StubPresenter();

        DeleteVariableInteractor interactor =
                new DeleteVariableInteractor(globalEnv, localEnv, presenter);

        DeleteVariableInputData input =
                new DeleteVariableInputData("score", true, "   ");

        interactor.execute(input);

        assertNull(presenter.lastSuccess);
        assertEquals("Variable type is required.", presenter.lastError);
    }
    @Test
    void deleteVariable_trimsWhitespace_success() throws EnvironmentException {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        StubPresenter presenter = new StubPresenter();

        var var = new NumericVariable("hp", true);
        Assign.assign(globalEnv, var, 100.0);

        DeleteVariableInteractor interactor =
                new DeleteVariableInteractor(globalEnv, localEnv, presenter);

        // Intentionally add whitespace in input
        DeleteVariableInputData input =
                new DeleteVariableInputData("  hp  ", true, "  Numeric  ");

        interactor.execute(input);

        assertThrows(EnvironmentException.class, () -> globalEnv.get(var));
        assertNotNull(presenter.lastSuccess);
        assertNull(presenter.lastError);
        assertEquals("hp", presenter.lastSuccess.getName());
        assertEquals("Numeric", presenter.lastSuccess.getType());
    }


}
