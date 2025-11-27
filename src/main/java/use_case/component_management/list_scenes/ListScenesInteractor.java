package use_case.component_management.list_scenes;

import use_case.component_management.SceneRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ListScenesInteractor implements ListScenesInputBoundary {
    private final SceneRepository sceneRepository;
    private final ListScenesOutputBoundary presenter;

    public ListScenesInteractor(SceneRepository sceneRepository, ListScenesOutputBoundary presenter) {
        this.sceneRepository = sceneRepository;
        this.presenter = presenter;
    }

    @Override
    public void listScenes() {
        var scenes = sceneRepository.getAllScenes();
        Map<String, List<String>> result = new HashMap<>();

        for (var scene : scenes) {
            result.put(scene.getName(), scene.getGameObjectNames());
        }

        presenter.presentScenes(result);
    }
}
