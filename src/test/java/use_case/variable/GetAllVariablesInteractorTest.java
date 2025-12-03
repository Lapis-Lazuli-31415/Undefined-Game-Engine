package use_case.variable;

import entity.scripting.environment.Environment;
import entity.scripting.environment.VariableMap;
import org.junit.jupiter.api.Test;
import use_case.variable.get.GetAllVariablesInputData;
import use_case.variable.get.GetAllVariablesInteractor;
import use_case.variable.get.GetAllVariablesOutputBoundary;
import use_case.variable.get.GetAllVariablesOutputData;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GetAllVariablesInteractorTest {


    private static class CapturingPresenter implements GetAllVariablesOutputBoundary {
        private GetAllVariablesOutputData lastOutput;

        @Override
        public void presentVariables(GetAllVariablesOutputData outputData) {
            this.lastOutput = outputData;
        }
    }


    private Environment createEnvironmentWithVariables(
            Map<String, Map<String, Object>> spec
    ) throws Exception {
        Environment env = new Environment();

        Field variablesField = Environment.class.getDeclaredField("variables");
        variablesField.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, VariableMap<?>> variables =
                (Map<String, VariableMap<?>>) variablesField.get(env);

        for (Map.Entry<String, Map<String, Object>> typeEntry : spec.entrySet()) {
            String typeName = typeEntry.getKey();
            VariableMap<Object> varMap = new VariableMap<>();

            for (Map.Entry<String, Object> varEntry : typeEntry.getValue().entrySet()) {
                String varName = varEntry.getKey();
                Object value = varEntry.getValue();
                varMap.getVariables().put(varName, value);
            }

            variables.put(typeName, varMap);
        }

        return env;
    }

    @Test
    void getAllVariables_returnsAllGlobalVariables() throws Exception {
        Environment globalEnv = createEnvironmentWithVariables(Map.of(
                "Integer", Map.of(
                        "score", 10,
                        "lives", 3
                ),
                "String", Map.of(
                        "playerName", "Lynn"
                )
        ));

        Environment localEnv = createEnvironmentWithVariables(Map.of(
                "Integer", Map.of(
                        "x", 1
                )
        ));

        CapturingPresenter presenter = new CapturingPresenter();
        GetAllVariablesInteractor interactor =
                new GetAllVariablesInteractor(globalEnv, localEnv, presenter);

        interactor.getAllVariables(new GetAllVariablesInputData(true));

        assertNotNull(presenter.lastOutput, "Presenter should have been called");
        List<GetAllVariablesOutputData.VariableData> vars =
                presenter.lastOutput.getVariables();

        assertEquals(3, vars.size());

        Map<String, GetAllVariablesOutputData.VariableData> byName =
                vars.stream().collect(Collectors.toMap(
                        GetAllVariablesOutputData.VariableData::getName,
                        Function.identity()
                ));

        assertTrue(byName.get("score").isGlobal());
        assertEquals("Integer", byName.get("score").getType());
        assertEquals("10", byName.get("score").getValue());

        assertTrue(byName.get("lives").isGlobal());
        assertEquals("Integer", byName.get("lives").getType());
        assertEquals("3", byName.get("lives").getValue());

        assertTrue(byName.get("playerName").isGlobal());
        assertEquals("String", byName.get("playerName").getType());
        assertEquals("Lynn", byName.get("playerName").getValue());
    }

    @Test
    void getAllVariables_returnsAllLocalVariables() throws Exception {
        Environment globalEnv = createEnvironmentWithVariables(Map.of(
                "Integer", Map.of(
                        "globalOnly", 999
                )
        ));

        Environment localEnv = createEnvironmentWithVariables(Map.of(
                "Float", Map.of(
                        "speed", 1.5f
                ),
                "Boolean", Map.of(
                        "isVisible", true
                )
        ));

        CapturingPresenter presenter = new CapturingPresenter();
        GetAllVariablesInteractor interactor =
                new GetAllVariablesInteractor(globalEnv, localEnv, presenter);

        interactor.getAllVariables(new GetAllVariablesInputData(false));

        assertNotNull(presenter.lastOutput, "Presenter should have been called");
        List<GetAllVariablesOutputData.VariableData> vars =
                presenter.lastOutput.getVariables();

        assertEquals(2, vars.size());

        Map<String, GetAllVariablesOutputData.VariableData> byName =
                vars.stream().collect(Collectors.toMap(
                        GetAllVariablesOutputData.VariableData::getName,
                        Function.identity()
                ));

        GetAllVariablesOutputData.VariableData speed = byName.get("speed");
        assertNotNull(speed);
        assertFalse(speed.isGlobal());
        assertEquals("Float", speed.getType());
        assertEquals("1.5", speed.getValue());

        GetAllVariablesOutputData.VariableData visible = byName.get("isVisible");
        assertNotNull(visible);
        assertFalse(visible.isGlobal());
        assertEquals("Boolean", visible.getType());
        assertEquals("true", visible.getValue());
    }
}
