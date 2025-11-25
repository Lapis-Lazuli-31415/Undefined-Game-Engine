package use_case.variable;

import entity.scripting.environment.Assign;
import entity.scripting.environment.Environment;
import entity.scripting.error.EnvironmentException;
import entity.scripting.expression.variable.BooleanVariable;
import entity.scripting.expression.variable.NumericVariable;
import entity.scripting.expression.variable.Variable;

public class UpdateVariableInteractor implements UpdateVariableInputBoundary {

    private final Environment globalEnv;
    private final Environment localEnv;
    private final UpdateVariableOutputBoundary presenter;

    public UpdateVariableInteractor(Environment globalEnv,
                                    Environment localEnv,
                                    UpdateVariableOutputBoundary presenter) {
        this.globalEnv = globalEnv;
        this.localEnv = localEnv;
        this.presenter = presenter;
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

        try {
            handleType(targetEnv, inputData, name, type, rawValue);
        } catch (EnvironmentException e) {
            presenter.prepareFailureView(e.getMessage());
        } catch (NumberFormatException e) {
            presenter.prepareFailureView("Invalid numeric value: '" + rawValue);
        } catch (IllegalArgumentException e) {
            presenter.prepareFailureView(e.getMessage());
        }
    }

    private void handleType(Environment env,
                            UpdateVariableInputData inputData,
                            String name,
                            String type,
                            String rawValue)
            throws EnvironmentException {

        if (type.equals("Numeric")) {
            handleNumeric(env, inputData, name, rawValue);
        } else if (type.equals("Boolean")) {
            handleBoolean(env, inputData, name, rawValue);
        } else {
            throw new IllegalArgumentException("Unsupported variable type: " + type);
        }
    }

    // helper functions

    private void handleNumeric(Environment env,
                               UpdateVariableInputData inputData,
                               String name,
                               String rawValue)
            throws EnvironmentException, NumberFormatException {

        NumericVariable variable = new NumericVariable(name, inputData.isGlobal());

        double parsed = Double.parseDouble(rawValue);

        Assign.assign(env, variable, parsed);

        UpdateVariableOutputData output = new UpdateVariableOutputData(
                name,
                Double.toString(parsed),
                inputData.isGlobal(),
                variable.getVariableType()
        );
        presenter.prepareSuccessView(output);
    }

    private void handleBoolean(Environment env,
                               UpdateVariableInputData inputData,
                               String name,
                               String rawValue)
            throws EnvironmentException {

        BooleanVariable variable = new BooleanVariable(name, inputData.isGlobal());

        String normalized = rawValue.toLowerCase();
        boolean parsed;
        if ("true".equals(normalized)) {
            parsed = true;
        } else if ("false".equals(normalized)) {
            parsed = false;
        } else {
            throw new IllegalArgumentException("Invalid boolean value: '" + rawValue);
        }

        Assign.assign(env, variable, parsed);

        UpdateVariableOutputData output = new UpdateVariableOutputData(
                name,
                Boolean.toString(parsed),
                inputData.isGlobal(),
                variable.getVariableType()
        );
        presenter.prepareSuccessView(output);
    }

    private static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }
}
