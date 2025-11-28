package use_case.component_management.select_game_object;

import use_case.component_management.SceneRepository;

public class SelectGameObjectInteractor implements SelectGameObjectInputBoundary {
    private final SceneRepository sceneRepository;
    private final SelectGameObjectOutputBoundary presenter;

    public SelectGameObjectInteractor(SceneRepository sceneRepository, SelectGameObjectOutputBoundary presenter) {
        this.sceneRepository = sceneRepository;
        this.presenter = presenter;
    }

    @Override
    public void selectGameObject(String sceneName, String gameObjectName) {
        var scene = sceneRepository.getSceneByName(sceneName);
        if (scene != null && scene.hasGameObject(gameObjectName)) {
            presenter.gameObjectSelected(sceneName, gameObjectName);
        }
    }
}
