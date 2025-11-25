package use_case.variable;

import entity.scripting.environment.Environment;
import entity.scripting.environment.Unassign;
import entity.scripting.error.EnvironmentException;
import entity.scripting.expression.variable.BooleanVariable;
import entity.scripting.expression.variable.NumericVariable;
import entity.scripting.expression.variable.Variable;

public class DeleteVariableInteractor implements DeleteVariableInputBoundary {

    private final Environment globalEnv;
    private final Environment localEnv;
    private final DeleteVariableOutputBoundary presenter;

    public DeleteVariableInteractor(Environment globalEnv,
                                    Environment localEnv,
                                    DeleteVariableOutputBoundary presenter) {
        this.globalEnv = globalEnv;
        this.localEnv = localEnv;
        this.presenter = presenter;
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
            handleType(targetEnv, inputData, name, type);
        } catch (EnvironmentException e) {
            presenter.prepareFailureView(e.getMessage());
        } catch (IllegalArgumentException e) {
            presenter.prepareFailureView(e.getMessage());
        }
    }


    private void handleType(Environment env,
                            DeleteVariableInputData inputData,
                            String name,
                            String type)
            throws EnvironmentException {

        if (type.equals("Numeric")) {
            handleNumeric(env, inputData, name);
        } else if (type.equals("Boolean")) {
            handleBoolean(env, inputData, name);
        } else {
            throw new IllegalArgumentException("Unsupported variable type: " + type);
        }
    }


    private void handleNumeric(Environment env,
                               DeleteVariableInputData inputData,
                               String name)
            throws EnvironmentException {

        NumericVariable variable = new NumericVariable(name, inputData.isGlobal());

        Unassign.unassign(env, variable);

        DeleteVariableOutputData output = new DeleteVariableOutputData(
                name,
                inputData.isGlobal(),
                variable.getVariableType()
        );
        presenter.prepareSuccessView(output);
    }

    private void handleBoolean(Environment env,
                               DeleteVariableInputData inputData,
                               String name)
            throws EnvironmentException {

        BooleanVariable variable = new BooleanVariable(name, inputData.isGlobal());

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
