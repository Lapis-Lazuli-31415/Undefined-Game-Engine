package use_case.variable;

import entity.scripting.environment.Assign;
import entity.scripting.environment.Environment;
import entity.scripting.environment.VariableMap;
import entity.scripting.error.EnvironmentException;
import entity.scripting.expression.variable.Variable;
import org.junit.jupiter.api.Test;
import use_case.variable.factory.DefaultVariableFactoryRegistry;
import use_case.variable.factory.NumericVariableFactory;
import use_case.variable.factory.VariableFactory;
import use_case.variable.update.UpdateVariableInputData;
import use_case.variable.update.UpdateVariableInteractor;
import use_case.variable.update.UpdateVariableOutputBoundary;
import use_case.variable.update.UpdateVariableOutputData;

import java.util.HashMap;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class UpdateVariableInteractorTest {


    private static class CapturingPresenter implements UpdateVariableOutputBoundary {
        UpdateVariableOutputData lastSuccess;
        String lastError;

        @Override
        public void prepareSuccessView(UpdateVariableOutputData data) {
            this.lastSuccess = data;
        }

        @Override
        public void prepareFailureView(String errorMessage) {
            this.lastError = errorMessage;
        }
    }


    private static class DummyEnvironment extends Environment {
        private final Map<String, VariableMap<?>> overrideVars;

        DummyEnvironment(Map<String, VariableMap<?>> overrideVars) {
            super();
            this.overrideVars = overrideVars;
        }

        @Override
        public Map<String, VariableMap<?>> getVariables() {
            return overrideVars;
        }
    }


    private static class BadNumericFactory implements VariableFactory {

        @Override
        public String getTypename() {
            return "BadNumeric";
        }

        @Override
        public Variable<?> createVariable(String name, boolean isGlobal) {
            return new NumericVariableFactory().createVariable(name, isGlobal);
        }

        @Override
        public Object parseValue(String rawValue) {
            return "not-a-number";
        }

        @Override
        public String formatValue(Object value) {
            return value.toString();
        }
    }


    private static class SingleTypeRegistry extends DefaultVariableFactoryRegistry {

        private final VariableFactory single;

        SingleTypeRegistry(VariableFactory factory) {
            super();
            this.single = factory;
        }

        @Override
        public VariableFactory get(String typename) {
            if (single.getTypename().equals(typename)) {
                return single;
            }
            return null;
        }

        @Override
        public java.util.List<String> getRegisteredTypes() {
            return java.util.List.of(single.getTypename());
        }
    }



    @Test
    void execute_nullName_failsWithMessage() {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        CapturingPresenter presenter = new CapturingPresenter();

        UpdateVariableInteractor interactor =
                new UpdateVariableInteractor(globalEnv, localEnv, presenter);

        UpdateVariableInputData input =
                new UpdateVariableInputData(null, "123", true, "Numeric");

        interactor.execute(input);

        assertEquals("Variable name cannot be empty.", presenter.lastError);
        assertNull(presenter.lastSuccess);
    }

    @Test
    void execute_emptyType_failsWithMessage() {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        CapturingPresenter presenter = new CapturingPresenter();

        UpdateVariableInteractor interactor =
                new UpdateVariableInteractor(globalEnv, localEnv, presenter);

        UpdateVariableInputData input =
                new UpdateVariableInputData(" score ", "1", true, "   ");

        interactor.execute(input);

        assertEquals("Variable type is required.", presenter.lastError);
        assertNull(presenter.lastSuccess);
    }

    @Test
    void execute_emptyValue_failsWithMessage() {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        CapturingPresenter presenter = new CapturingPresenter();

        UpdateVariableInteractor interactor =
                new UpdateVariableInteractor(globalEnv, localEnv, presenter);

        UpdateVariableInputData input =
                new UpdateVariableInputData("score", "   ", true, "Numeric");

        interactor.execute(input);

        assertEquals("Variable value cannot be empty.", presenter.lastError);
        assertNull(presenter.lastSuccess);
    }


    @Test
    void execute_existingVariableTypeMismatch_fails() throws EnvironmentException {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        CapturingPresenter presenter = new CapturingPresenter();
        DefaultVariableFactoryRegistry registry = new DefaultVariableFactoryRegistry();

        NumericVariableFactory numericFactory = new NumericVariableFactory();
        Variable<?> existing = numericFactory.createVariable("score", true);
        Object value = numericFactory.parseValue("1.0");
        Assign.assign(globalEnv, (Variable) existing, value);

        UpdateVariableInteractor interactor =
                new UpdateVariableInteractor(globalEnv, localEnv, presenter, registry);

        UpdateVariableInputData input =
                new UpdateVariableInputData("score", "true", true, "Boolean");

        interactor.execute(input);

        assertEquals(
                "Invalid update: existing Numeric variable score cannot be updated as Boolean.",
                presenter.lastError
        );
        assertNull(presenter.lastSuccess);
    }


    @Test
    void execute_unsupportedType_reportsIllegalArgumentMessage() {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        CapturingPresenter presenter = new CapturingPresenter();
        DefaultVariableFactoryRegistry registry = new DefaultVariableFactoryRegistry();

        UpdateVariableInteractor interactor =
                new UpdateVariableInteractor(globalEnv, localEnv, presenter, registry);

        UpdateVariableInputData input =
                new UpdateVariableInputData("foo", "bar", true, "UnknownType");

        interactor.execute(input);

        assertEquals("Unsupported variable type: UnknownType", presenter.lastError);
        assertNull(presenter.lastSuccess);
    }

    @Test
    void execute_numericParseError_reportsNumberFormatMessage() {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        CapturingPresenter presenter = new CapturingPresenter();
        DefaultVariableFactoryRegistry registry = new DefaultVariableFactoryRegistry();

        UpdateVariableInteractor interactor =
                new UpdateVariableInteractor(globalEnv, localEnv, presenter, registry);

        UpdateVariableInputData input =
                new UpdateVariableInputData("score", "abc", true, "Numeric");

        interactor.execute(input);

        assertEquals("Invalid numeric value: abc", presenter.lastError);
        assertNull(presenter.lastSuccess);
    }

    @Test
    void execute_booleanParseError_reportsIllegalArgumentMessage() {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        CapturingPresenter presenter = new CapturingPresenter();
        DefaultVariableFactoryRegistry registry = new DefaultVariableFactoryRegistry();

        UpdateVariableInteractor interactor =
                new UpdateVariableInteractor(globalEnv, localEnv, presenter, registry);

        UpdateVariableInputData input =
                new UpdateVariableInputData("flag", "nottrue", true, "Boolean");

        interactor.execute(input);

        assertEquals("Invalid boolean value: nottrue", presenter.lastError);
        assertNull(presenter.lastSuccess);
    }


    @Test
    void execute_environmentExceptionFromAssign_isReported() {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        CapturingPresenter presenter = new CapturingPresenter();

        SingleTypeRegistry registry = new SingleTypeRegistry(new BadNumericFactory());

        UpdateVariableInteractor interactor =
                new UpdateVariableInteractor(globalEnv, localEnv, presenter, registry);

        UpdateVariableInputData input =
                new UpdateVariableInputData("x", "anything", true, "BadNumeric");

        interactor.execute(input);

        assertNotNull(presenter.lastError);
        assertTrue(
                presenter.lastError.contains("Invalid value for variable x:"),
                "Expected EnvironmentException message, got: " + presenter.lastError
        );
        assertNull(presenter.lastSuccess);
    }


    @Test
    void execute_successLocalNumeric_updatesEnvironmentAndCallsPresenter() {

        Map<String, VariableMap<?>> fakeGlobalVars = new HashMap<>();
        fakeGlobalVars.put("Numeric", new VariableMap<Double>());
        Environment globalEnv = new DummyEnvironment(fakeGlobalVars);

        Environment localEnv = new Environment();

        CapturingPresenter presenter = new CapturingPresenter();
        DefaultVariableFactoryRegistry registry = new DefaultVariableFactoryRegistry();

        UpdateVariableInteractor interactor =
                new UpdateVariableInteractor(globalEnv, localEnv, presenter, registry);

        UpdateVariableInputData input =
                new UpdateVariableInputData("score", "3.5", false, "Numeric");

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);

        UpdateVariableOutputData out = presenter.lastSuccess;
        assertEquals("score", out.getName());
        assertEquals("3.5", out.getValue());
        assertFalse(out.isGlobal());
        assertEquals("Numeric", out.getType());

        assertTrue(globalEnv.getVariables().containsKey("Numeric"));

        VariableMap<?> globalNumericMap = globalEnv.getVariables().get("Numeric");
        assertTrue(globalNumericMap.getVariables().isEmpty());

        Map<String, VariableMap<?>> localVars = localEnv.getVariables();
        assertTrue(localVars.containsKey("Numeric"));
        VariableMap<?> localNumericMap = localVars.get("Numeric");
        assertFalse(localNumericMap.getVariables().isEmpty());
        assertTrue(localNumericMap.getVariables().containsKey("score"));
    }

    @Test
    void execute_successGlobalNumeric_printEnvironmentEmptyLocal() {

        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();

        CapturingPresenter presenter = new CapturingPresenter();
        DefaultVariableFactoryRegistry registry = new DefaultVariableFactoryRegistry();

        UpdateVariableInteractor interactor =
                new UpdateVariableInteractor(globalEnv, localEnv, presenter, registry);

        UpdateVariableInputData input =
                new UpdateVariableInputData("health", "10.0", true, "Numeric");

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);
        assertEquals("health", presenter.lastSuccess.getName());
        assertTrue(presenter.lastSuccess.isGlobal());

        assertTrue(globalEnv.getVariables().containsKey("Numeric"));


        assertTrue(localEnv.getVariables().isEmpty());
    }

    @Test
    void execute_existingVariableSameType_allowsUpdate() throws EnvironmentException {
        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();
        CapturingPresenter presenter = new CapturingPresenter();
        DefaultVariableFactoryRegistry registry = new DefaultVariableFactoryRegistry();

        NumericVariableFactory numericFactory = new NumericVariableFactory();
        Variable<?> existing = numericFactory.createVariable("score", true);
        Object initialValue = numericFactory.parseValue("1.0");
        Assign.assign(globalEnv, (Variable) existing, initialValue);

        UpdateVariableInteractor interactor =
                new UpdateVariableInteractor(globalEnv, localEnv, presenter, registry);

        UpdateVariableInputData input =
                new UpdateVariableInputData("score", "2.5", true, "Numeric");

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);

        UpdateVariableOutputData out = presenter.lastSuccess;
        assertEquals("score", out.getName());
        assertEquals("2.5", out.getValue());
        assertTrue(out.isGlobal());
        assertEquals("Numeric", out.getType());

        Map<String, VariableMap<?>> vars = globalEnv.getVariables();
        assertTrue(vars.containsKey("Numeric"));
        VariableMap<?> numericMap = vars.get("Numeric");
        assertTrue(numericMap.getVariables().containsKey("score"));
    }

}