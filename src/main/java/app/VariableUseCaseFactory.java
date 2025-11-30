package app;

import entity.scripting.environment.Environment;
import interface_adapter.variable.*;
import interface_adapter.variable.delete.DeleteVariableController;
import interface_adapter.variable.delete.GlobalVariableDeletePresenter;
import interface_adapter.variable.delete.LocalVariableDeletePresenter;
import interface_adapter.variable.get.GetAllGlobalVariablesPresenter;
import interface_adapter.variable.get.GetAllLocalVariablesPresenter;
import interface_adapter.variable.get.GetAllVariablesController;
import interface_adapter.variable.update.GlobalVariableUpdatePresenter;
import interface_adapter.variable.update.LocalVariableUpdatePresenter;
import interface_adapter.variable.update.UpdateVariableController;
import use_case.variable.delete.DeleteVariableInteractor;
import use_case.variable.get.GetAllVariablesInteractor;
import use_case.variable.update.UpdateVariableInteractor;

public class VariableUseCaseFactory {

    public static VariableWiring create(
            Environment globalEnvironment,
            Environment localEnvironment,
            GlobalVariableViewModel globalViewModel) {

        if (globalViewModel == null) {
            globalViewModel = new GlobalVariableViewModel();
        }

        LocalVariableViewModel localViewModel = new LocalVariableViewModel();

        GlobalVariableUpdatePresenter globalUpdatePresenter =
                new GlobalVariableUpdatePresenter(globalViewModel);
        GlobalVariableDeletePresenter globalDeletePresenter =
                new GlobalVariableDeletePresenter(globalViewModel);
        LocalVariableUpdatePresenter localUpdatePresenter =
                new LocalVariableUpdatePresenter(localViewModel);
        LocalVariableDeletePresenter localDeletePresenter =
                new LocalVariableDeletePresenter(localViewModel);

        GetAllGlobalVariablesPresenter getAllGlobalPresenter =
                new GetAllGlobalVariablesPresenter(globalViewModel);
        GetAllLocalVariablesPresenter getAllLocalPresenter =
                new GetAllLocalVariablesPresenter(localViewModel);

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

        GetAllVariablesInteractor getAllGlobalInteractor =
                new GetAllVariablesInteractor(
                        globalEnvironment,
                        globalEnvironment,
                        getAllGlobalPresenter
                );
        GetAllVariablesInteractor getAllLocalInteractor =
                new GetAllVariablesInteractor(
                        globalEnvironment,
                        localEnvironment,
                        getAllLocalPresenter
                );

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

        GetAllVariablesController getAllController =
                new GetAllVariablesController(
                        getAllGlobalInteractor,
                        getAllLocalInteractor
                );

        return new VariableWiring(
                globalViewModel,
                localViewModel,
                updateController,
                deleteController,
                getAllController
        );
    }


    public static class VariableWiring {
        private final GlobalVariableViewModel globalViewModel;
        private final LocalVariableViewModel localViewModel;
        private final UpdateVariableController updateController;
        private final DeleteVariableController deleteController;
        private final GetAllVariablesController getAllController;

        public VariableWiring(
                GlobalVariableViewModel globalViewModel,
                LocalVariableViewModel localViewModel,
                UpdateVariableController updateController,
                DeleteVariableController deleteController,
                GetAllVariablesController getAllController) {
            this.globalViewModel = globalViewModel;
            this.localViewModel = localViewModel;
            this.updateController = updateController;
            this.deleteController = deleteController;
            this.getAllController = getAllController;
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

        public GetAllVariablesController getGetAllController() {
            return getAllController;
        }
    }
}