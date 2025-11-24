package use_case.create_scene;

import entity.Scene;

public class CreateSceneInteractor implements CreateSceneInputBoundary {
    private final CreateSceneUserDataAccessInterface dataAccessObject;
    private final CreateSceneOutputBoundary presenter;

    public CreateSceneInteractor(CreateSceneUserDataAccessInterface dataAccessObject,
                CreateSceneOutputBoundary presenter) {
            this.dataAccessObject = dataAccessObject;
            this.presenter = presenter;
        }

        @Override
        public void execute(CreateSceneInputData inputData) {
            String name = inputData.getName();

            if (name == null || name.isBlank()) {
                presenter.prepareFailureView("Scene name cannot be empty.");
                return;
            }

            if (dataAccessObject.existsByName(name)) {
                presenter.prepareFailureView("Scene with this name already exists.");
                return;
            }

            Scene newScene = Scene.create(name);
            dataAccessObject.save(newScene);

            presenter.prepareSuccessView(
                    new CreateSceneOutputData(newScene.getId(), newScene.getName())
            );
        }
    }
}
