package use_case.variable.get;

import entity.scripting.environment.Environment;
import entity.scripting.environment.VariableMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GetAllVariablesInteractor implements GetAllVariablesInputBoundary {

    private final Environment globalEnvironment;
    private final Environment localEnvironment;
    private final GetAllVariablesOutputBoundary presenter;

    public GetAllVariablesInteractor(
            Environment globalEnvironment,
            Environment localEnvironment,
            GetAllVariablesOutputBoundary presenter) {
        this.globalEnvironment = globalEnvironment;
        this.localEnvironment = localEnvironment;
        this.presenter = presenter;
    }

    @Override
    public void getAllVariables(GetAllVariablesInputData inputData) {
        Environment targetEnvironment = inputData.isGlobal()
                ? globalEnvironment
                : localEnvironment;

        List<GetAllVariablesOutputData.VariableData> variables = extractVariablesFromEnvironment(
                targetEnvironment,
                inputData.isGlobal()
        );

        GetAllVariablesOutputData outputData = new GetAllVariablesOutputData(variables);
        presenter.presentVariables(outputData);
    }

    private List<GetAllVariablesOutputData.VariableData> extractVariablesFromEnvironment(
            Environment environment,
            boolean isGlobal) {

        List<GetAllVariablesOutputData.VariableData> result = new ArrayList<>();

        Map<String, VariableMap<?>> variableMaps = environment.getVariables();

        for (Map.Entry<String, VariableMap<?>> entry : variableMaps.entrySet()) {
            String type = entry.getKey();
            VariableMap<?> varMap = entry.getValue();

            for (String name : varMap.getNames()) {
                Object value = varMap.get(name);
                result.add(new GetAllVariablesOutputData.VariableData(
                        name,
                        type,
                        value.toString(),
                        isGlobal
                ));
            }
        }

        return result;
    }
}