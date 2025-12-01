package use_case.variable.delete;

import entity.scripting.environment.Environment;
import entity.scripting.environment.Unassign;
import entity.scripting.error.EnvironmentException;
import entity.scripting.expression.variable.Variable;
import use_case.variable.factory.VariableFactory;
import use_case.variable.factory.DefaultVariableFactoryRegistry;

public class DeleteVariableInteractor implements DeleteVariableInputBoundary {

    private final Environment globalEnv;
    private final Environment localEnv;
    private final DeleteVariableOutputBoundary presenter;
    private final DefaultVariableFactoryRegistry factoryRegistry;

    public DeleteVariableInteractor(Environment globalEnv,
                                    Environment localEnv,
                                    DeleteVariableOutputBoundary presenter) {
        this(globalEnv, localEnv, presenter, new DefaultVariableFactoryRegistry());
    }

    public DeleteVariableInteractor(Environment globalEnv,
                                    Environment localEnv,
                                    DeleteVariableOutputBoundary presenter,
                                    DefaultVariableFactoryRegistry factoryRegistry) {
        this.globalEnv = globalEnv;
        this.localEnv = localEnv;
        this.presenter = presenter;
        this.factoryRegistry = factoryRegistry;
    }

    @Override
    public void execute(DeleteVariableInputData inputData) {
        String name = safeTrim(inputData.getName());
        String type = safeTrim(inputData.getType());

        if (name.isEmpty()) {
            presenter.prepareFailureView("Variable name cannot be empty.");
            return;
        }

        if (type.isEmpty()) {
            presenter.prepareFailureView("Variable type is required.");
            return;
        }

        Environment targetEnv = inputData.isGlobal() ? globalEnv : localEnv;

        try {
            handleWithFactory(targetEnv, inputData, name, type);
        } catch (EnvironmentException e) {
            presenter.prepareFailureView(e.getMessage());
        } catch (IllegalArgumentException e) {
            presenter.prepareFailureView(e.getMessage());
        }
    }

    private void handleWithFactory(Environment env,
                                   DeleteVariableInputData inputData,
                                   String name,
                                   String type)
            throws EnvironmentException {

        VariableFactory factory = factoryRegistry.get(type);

        if (factory == null) {
            throw new IllegalArgumentException("Unsupported variable type: " + type);
        }

        Variable<?> variable = factory.createVariable(name, inputData.isGlobal());

        Unassign.unassign(env, variable);

        DeleteVariableOutputData output = new DeleteVariableOutputData(
                name,
                inputData.isGlobal(),
                variable.getVariableType()
        );
        presenter.prepareSuccessView(output);
    }

    private static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }
}
