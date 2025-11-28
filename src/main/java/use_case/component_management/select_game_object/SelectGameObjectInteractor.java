package use_case.component_management.select_game_object;

import entity.GameObject;
import use_case.component_management.SceneRepository;

public class SelectGameObjectInteractor implements SelectGameObjectInputBoundary {
    private final SceneRepository sceneRepository;
    private final SelectGameObjectOutputBoundary presenter;

    public SelectGameObjectInteractor(SceneRepository sceneRepository, SelectGameObjectOutputBoundary presenter) {
        this.sceneRepository = sceneRepository;
        this.presenter = presenter;
    }

    @Override
    public void selectGameObject(String sceneName, GameObject gameObject) {
        var scene = sceneRepository.getSceneByName(sceneName);
        if (scene != null && scene.hasGameObject(gameObject)) {
            presenter.gameObjectSelected(scene, gameObject);
        }
    }
}
