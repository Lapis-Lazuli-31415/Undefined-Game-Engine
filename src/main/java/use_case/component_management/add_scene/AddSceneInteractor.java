package use_case.component_management.add_scene;

import entity.Scene;
import use_case.component_management.SceneRepository;

import java.util.UUID;

public class AddSceneInteractor implements AddSceneInputBoundary {

    private final SceneRepository repo;
    private final AddSceneOutputBoundary presenter;

    public AddSceneInteractor(SceneRepository repo, AddSceneOutputBoundary presenter) {
        this.repo = repo;
        this.presenter = presenter;
    }

    @Override
    public void addScene(String sceneName) {
        Scene scene = new Scene(UUID.randomUUID(), sceneName, null);
        repo.saveScene(scene);
        presenter.sceneCreated(scene);
    }
}
