package use_case.variable;

import entity.scripting.environment.Environment;
import entity.scripting.error.EnvironmentException;
import entity.scripting.expression.variable.Variable;
import org.junit.jupiter.api.Test;
import use_case.variable.delete.DeleteVariableInputData;
import use_case.variable.delete.DeleteVariableInteractor;
import use_case.variable.delete.DeleteVariableOutputBoundary;
import use_case.variable.delete.DeleteVariableOutputData;
import use_case.variable.factory.DefaultVariableFactoryRegistry;
import use_case.variable.factory.VariableFactory;

import static org.junit.jupiter.api.Assertions.*;

class DeleteVariableInteractorTest {


    private static class TestPresenter implements DeleteVariableOutputBoundary {
        DeleteVariableOutputData lastSuccess;
        String lastError;

        @Override
        public void prepareSuccessView(DeleteVariableOutputData data) {
            this.lastSuccess = data;
            this.lastError = null;
        }

        @Override
        public void prepareFailureView(String errorMessage) {
            this.lastError = errorMessage;
            this.lastSuccess = null;
        }
    }


    private static class NoOpEnvironment extends Environment {
        private Variable<?> lastDeleted;

        @Override
        public <T> void delete(Variable<T> variable) throws EnvironmentException {
            this.lastDeleted = variable;
        }

        public Variable<?> getLastDeleted() {
            return lastDeleted;
        }
    }


    private static String findSupportedType(DefaultVariableFactoryRegistry registry) {
        String[] candidates = {
                "int", "Integer",
                "double", "Double",
                "float", "Float",
                "long", "Long",
                "boolean", "Boolean",
                "string", "String",
                "text", "Text",
                "number", "Number"
        };

        for (String candidate : candidates) {
            VariableFactory factory = registry.get(candidate);
            if (factory != null) {
                return candidate;
            }
        }

        fail("No supported variable type found in DefaultVariableFactoryRegistry for test candidates");
        return null;
    }


    @Test
    void execute_fails_whenNameIsNull() {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        TestPresenter presenter = new TestPresenter();

        DeleteVariableInteractor interactor =
                new DeleteVariableInteractor(globalEnv, localEnv, presenter);

        DeleteVariableInputData input =
                new DeleteVariableInputData(null, true, "whatever");

        interactor.execute(input);

        assertNull(presenter.lastSuccess);
        assertEquals("Variable name cannot be empty.", presenter.lastError);
    }

    @Test
    void execute_fails_whenNameIsBlank() {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        TestPresenter presenter = new TestPresenter();

        DeleteVariableInteractor interactor =
                new DeleteVariableInteractor(globalEnv, localEnv, presenter);

        DeleteVariableInputData input =
                new DeleteVariableInputData("   ", false, "whatever");

        interactor.execute(input);

        assertNull(presenter.lastSuccess);
        assertEquals("Variable name cannot be empty.", presenter.lastError);
    }

    @Test
    void execute_fails_whenTypeIsNull() {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        TestPresenter presenter = new TestPresenter();

        DeleteVariableInteractor interactor =
                new DeleteVariableInteractor(globalEnv, localEnv, presenter);

        DeleteVariableInputData input =
                new DeleteVariableInputData("score", true, null);

        interactor.execute(input);

        assertNull(presenter.lastSuccess);
        assertEquals("Variable type is required.", presenter.lastError);
    }

    @Test
    void execute_fails_whenTypeIsBlank() {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        TestPresenter presenter = new TestPresenter();

        DeleteVariableInteractor interactor =
                new DeleteVariableInteractor(globalEnv, localEnv, presenter);

        DeleteVariableInputData input =
                new DeleteVariableInputData("score", false, "   ");

        interactor.execute(input);

        assertNull(presenter.lastSuccess);
        assertEquals("Variable type is required.", presenter.lastError);
    }

    @Test
    void execute_fails_whenTypeUnsupported_triggersIllegalArgumentCatch() {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        TestPresenter presenter = new TestPresenter();

        DeleteVariableInteractor interactor =
                new DeleteVariableInteractor(globalEnv, localEnv, presenter);

        String unsupportedType = "this-type-does-not-exist";
        DeleteVariableInputData input =
                new DeleteVariableInputData("score", true, unsupportedType);

        interactor.execute(input);

        assertNull(presenter.lastSuccess);
        assertEquals("Unsupported variable type: " + unsupportedType, presenter.lastError);
    }

    @Test
    void execute_fails_whenEnvironmentThrows_triggersEnvironmentExceptionCatch() {
        Environment globalEnv = new Environment(); // real env that *will* throw on delete
        Environment localEnv = new Environment();
        TestPresenter presenter = new TestPresenter();

        DefaultVariableFactoryRegistry registry = new DefaultVariableFactoryRegistry();
        String validType = findSupportedType(registry);

        DeleteVariableInteractor interactor =
                new DeleteVariableInteractor(globalEnv, localEnv, presenter, registry);

        DeleteVariableInputData input =
                new DeleteVariableInputData("score", false, validType);

        interactor.execute(input);

        assertNull(presenter.lastSuccess);
        assertNotNull(presenter.lastError);
    }

    @Test
    void execute_succeeds_forGlobalVariable_usesTrimmedName() {
        NoOpEnvironment globalEnv = new NoOpEnvironment();
        Environment localEnv = new Environment();
        TestPresenter presenter = new TestPresenter();

        DefaultVariableFactoryRegistry registry = new DefaultVariableFactoryRegistry();
        String validType = findSupportedType(registry);

        DeleteVariableInteractor interactor =
                new DeleteVariableInteractor(globalEnv, localEnv, presenter, registry);

        DeleteVariableInputData input =
                new DeleteVariableInputData("  score  ", true, validType);

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);

        DeleteVariableOutputData out = presenter.lastSuccess;

        assertEquals("score", out.getName());
        assertTrue(out.isGlobal());
        assertNotNull(out.getType());
        assertNotNull(globalEnv.getLastDeleted());
    }
}
