package app;

import entity.scripting.environment.Environment;
import interface_adapter.variable.*;
import use_case.variable.*;

/**
 * Factory for wiring up the Variable use cases.
 * This is the ONLY place that knows about Presenters and Interactors.
 * Views only interact with ViewModels and Controllers.
 */
public class VariableUseCaseFactory {

    /**
     * Creates and wires all components for Variable management.
     *
     * @param globalEnvironment The global variable environment
     * @param localEnvironment The local variable environment for the current game object
     * @return A bundle containing all wired components needed by the View
     */
    public static VariableWiring create(
            Environment globalEnvironment,
            Environment localEnvironment) {

        // 1. Create ViewModels
        GlobalVariableViewModel globalViewModel = new GlobalVariableViewModel();
        LocalVariableViewModel localViewModel = new LocalVariableViewModel();

        // 2. Create Presenters (View layer doesn't know about these!)
        GlobalVariableUpdatePresenter globalUpdatePresenter =
                new GlobalVariableUpdatePresenter(globalViewModel);
        GlobalVariableDeletePresenter globalDeletePresenter =
                new GlobalVariableDeletePresenter(globalViewModel);
        LocalVariableUpdatePresenter localUpdatePresenter =
                new LocalVariableUpdatePresenter(localViewModel);
        LocalVariableDeletePresenter localDeletePresenter =
                new LocalVariableDeletePresenter(localViewModel);

        // 3. Create Interactors (View layer doesn't know about these!)
        // Global interactor uses globalEnv for both parameters
        UpdateVariableInteractor globalUpdateInteractor =
                new UpdateVariableInteractor(
                        globalEnvironment,
                        globalEnvironment,
                        globalUpdatePresenter
                );
        DeleteVariableInteractor globalDeleteInteractor =
                new DeleteVariableInteractor(
                        globalEnvironment,
                        globalEnvironment,
                        globalDeletePresenter
                );

        // Local interactor uses globalEnv and localEnv
        UpdateVariableInteractor localUpdateInteractor =
                new UpdateVariableInteractor(
                        globalEnvironment,
                        localEnvironment,
                        localUpdatePresenter
                );
        DeleteVariableInteractor localDeleteInteractor =
                new DeleteVariableInteractor(
                        globalEnvironment,
                        localEnvironment,
                        localDeletePresenter
                );

        // 4. Create Controllers (View knows about these)
        UpdateVariableController updateController =
                new UpdateVariableController(
                        localUpdateInteractor,
                        globalUpdateInteractor
                );
        DeleteVariableController deleteController =
                new DeleteVariableController(
                        localDeleteInteractor,
                        globalDeleteInteractor
                );

        // 5. Return everything the View needs (ViewModels + Controllers only)
        return new VariableWiring(
                globalViewModel,
                localViewModel,
                updateController,
                deleteController
        );
    }

    /**
     * Data class that bundles all components the View layer needs.
     * Notice: This only contains ViewModels and Controllers.
     * Presenters and Interactors are hidden inside the factory.
     */
    public static class VariableWiring {
        private final GlobalVariableViewModel globalViewModel;
        private final LocalVariableViewModel localViewModel;
        private final UpdateVariableController updateController;
        private final DeleteVariableController deleteController;

        public VariableWiring(
                GlobalVariableViewModel globalViewModel,
                LocalVariableViewModel localViewModel,
                UpdateVariableController updateController,
                DeleteVariableController deleteController) {
            this.globalViewModel = globalViewModel;
            this.localViewModel = localViewModel;
            this.updateController = updateController;
            this.deleteController = deleteController;
        }

        public GlobalVariableViewModel getGlobalViewModel() {
            return globalViewModel;
        }

        public LocalVariableViewModel getLocalViewModel() {
            return localViewModel;
        }

        public UpdateVariableController getUpdateController() {
            return updateController;
        }

        public DeleteVariableController getDeleteController() {
            return deleteController;
        }
    }
}