package use_case.component_management.select_scene;

import use_case.component_management.SceneRepository;

public class SelectSceneInteractor implements SelectSceneInputBoundary{
    private final SceneRepository sceneRepository;
    private final SelectSceneOutputBoundary presenter;

    public SelectSceneInteractor(SceneRepository sceneRepository, SelectSceneOutputBoundary presenter) {
        this.sceneRepository = sceneRepository;
        this.presenter = presenter;
    }

    @Override
    public void selectScene(String sceneName) {
        var scene = sceneRepository.getSceneByName(sceneName);
        if (scene != null) {
            presenter.sceneSelected(scene);
        }
    }
}
