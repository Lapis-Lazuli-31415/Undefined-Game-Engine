package app;

import interface_adapter.variable.VariableController;
import interface_adapter.variable.VariableViewModel;
import entity.GameObject;
import entity.scripting.environment.Environment;
import use_case.variable.UpdateVariableInputBoundary;
import use_case.variable.UpdateVariableInteractor;
import use_case.variable.UpdateVariableOutputBoundary;
import use_case.variable.DeleteVariableInputBoundary;
import use_case.variable.DeleteVariableInteractor;
import use_case.variable.DeleteVariableOutputBoundary;
import interface_adapter.variable.VariablePresenter;

public class VariableUseCaseFactory {

    private VariableUseCaseFactory() {}

    public static VariableController create(Environment globalEnv,
                                            Environment localEnv,
                                            VariableViewModel viewModel) {

        VariablePresenter presenter = new VariablePresenter(viewModel);

        UpdateVariableOutputBoundary updatePresenter = presenter;
        DeleteVariableOutputBoundary deletePresenter = presenter;

        UpdateVariableInputBoundary updateInteractor =
                new UpdateVariableInteractor(globalEnv, localEnv, updatePresenter);

        DeleteVariableInputBoundary deleteInteractor =
                new DeleteVariableInteractor(globalEnv, localEnv, deletePresenter);

        return new VariableController(updateInteractor, deleteInteractor);
    }
}
