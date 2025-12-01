package use_case.variable.update;

import entity.scripting.environment.Assign;
import entity.scripting.environment.Environment;
import entity.scripting.environment.VariableMap;
import entity.scripting.error.EnvironmentException;
import entity.scripting.expression.variable.Variable;
import use_case.variable.factory.VariableFactory;
import use_case.variable.factory.DefaultVariableFactoryRegistry;

import java.util.Map;

public class UpdateVariableInteractor implements UpdateVariableInputBoundary {

    private final Environment globalEnv;
    private final Environment localEnv;
    private final UpdateVariableOutputBoundary presenter;
    private final DefaultVariableFactoryRegistry factoryRegistry;

    public UpdateVariableInteractor(Environment globalEnv,
                                    Environment localEnv,
                                    UpdateVariableOutputBoundary presenter){
        this(globalEnv,localEnv,presenter,new DefaultVariableFactoryRegistry());
    }

    public UpdateVariableInteractor(Environment globalEnv,
                                    Environment localEnv,
                                    UpdateVariableOutputBoundary presenter,
                                    DefaultVariableFactoryRegistry factoryRegistry) {
        this.globalEnv = globalEnv;
        this.localEnv = localEnv;
        this.presenter = presenter;
        this.factoryRegistry = factoryRegistry;
    }

    @Override
    public void execute(UpdateVariableInputData inputData) {
        String name = safeTrim(inputData.getName());
        String type = safeTrim(inputData.getType());
        String rawValue = safeTrim(inputData.getValue());

        if (name.isEmpty()) {
            presenter.prepareFailureView("Variable name cannot be empty.");
            return;
        }

        if (type.isEmpty()) {
            presenter.prepareFailureView("Variable type is required.");
            return;
        }

        if (rawValue.isEmpty()) {
            presenter.prepareFailureView("Variable value cannot be empty.");
            return;
        }

        Environment targetEnv = inputData.isGlobal() ? globalEnv : localEnv;

        // check type
        Variable<?> existing = findExistingVariable(targetEnv, name);
        if (existing != null) {
            String existingType = existing.getVariableType();
            if (!existingType.equals(type)) {
                presenter.prepareFailureView("Invalid update: existing " + existingType +
                        " variable " + name + " cannot be updated as " + type + ".");
                return;
            }
        }

        try{
            handleWithFactory(targetEnv, inputData, name, type, rawValue);

            // for debug
            System.out.println("\n========================================");
            System.out.println("AFTER UPDATE - " + (inputData.isGlobal() ? "GLOBAL" : "LOCAL") + " variable: " + name);
            System.out.println("========================================");
            printEnvironment("GLOBAL", globalEnv);
            printEnvironment("LOCAL", localEnv);
            System.out.println("========================================\n");

        } catch (EnvironmentException e){
            presenter.prepareFailureView(e.getMessage());
        } catch (NumberFormatException e){
            //numeric parse error from NumericVariableFactory
            presenter.prepareFailureView("Invalid numeric value: " + rawValue);
        } catch (IllegalArgumentException e){
            // boolean parse error or unknown type
            presenter.prepareFailureView(e.getMessage());
        }
    }

    private Variable<?> findExistingVariable(Environment env, String name) {
        for (String typeName : factoryRegistry.getRegisteredTypes()) {
            VariableFactory factory = factoryRegistry.get(typeName);
            Variable<?> probe = factory.createVariable(name, false);

            try {
                env.get(probe);
                return probe;
            } catch (EnvironmentException ignored) { }
        }
        return null;
    }

    private void handleWithFactory(Environment env,
                                   UpdateVariableInputData inputData,
                                   String name,
                                   String type,
                                   String rawValue)
            throws EnvironmentException {

        VariableFactory factory = factoryRegistry.get(type);

        if  (factory == null) {
            throw new IllegalArgumentException("Unsupported variable type: " + type);
        }

        Variable<?> variable = factory.createVariable(name, inputData.isGlobal());
        Object parsedValue = factory.parseValue(rawValue);

        @SuppressWarnings({"rawtypes", "unchecked"})
        Variable rawVariable = (Variable) variable;
        Assign.assign(env, rawVariable, parsedValue);

        String formattedValue = factory.formatValue(parsedValue);

        UpdateVariableOutputData output = new UpdateVariableOutputData(
                name,
                formattedValue,
                inputData.isGlobal(),
                variable.getVariableType()
        );
        presenter.prepareSuccessView(output);
    }

    //for debug
    private void printEnvironment(String label, Environment env) {
        System.out.println("\n" + label + " Environment:");
        Map<String, VariableMap<?>> vars = env.getVariables();

        if (vars.isEmpty()) {
            System.out.println("  (empty)");
            return;
        }

        int count = 0;
        for (Map.Entry<String, VariableMap<?>> entry : vars.entrySet()) {
            String typeName = entry.getKey();
            VariableMap<?> varMap = entry.getValue();
            Map<String, ?> variables = varMap.getVariables();

            for (Map.Entry<String, ?> varEntry : variables.entrySet()) {
                System.out.println("  [" + typeName + "] " + varEntry.getKey() + " = " + varEntry.getValue());
                count++;
            }
        }

        if (count == 0) {
            System.out.println("  (no variables)");
        } else {
            System.out.println("  Total: " + count + " variable(s)");
        }
    }

    private static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }
}