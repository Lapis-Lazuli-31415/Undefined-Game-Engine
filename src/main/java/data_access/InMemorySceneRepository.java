package data_access;

import use_case.component_management.SceneRepository;
import entity.Scene;
import entity.GameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Simple in-memory SceneRepository for demo/wiring.
 */
public class InMemorySceneRepository implements SceneRepository {

    private final List<Scene> scenes = new ArrayList<>();
    private Scene currentScene;

    public InMemorySceneRepository() {
        Scene defaultScene = new Scene(UUID.randomUUID(), "Default Scene", new ArrayList<>());
        scenes.add(defaultScene);
        this.currentScene = defaultScene;
    }

    @Override
    public List<Scene> getAllScenes() {
        return List.copyOf(scenes);
    }

    @Override
    public Scene getSceneByName(String name) {
        return scenes.stream().filter(s -> s.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public void saveScene(Scene scene) {
        for (int i = 0; i < scenes.size(); i++) {
            if (scenes.get(i).getName().equals(scene.getName())) {
                scenes.set(i, scene);
                return;
            }
        }
        scenes.add(scene);
    }

    @Override
    public void setCurrentScene(Scene scene) {
        this.currentScene = scene;
    }

    @Override
    public Scene getCurrentScene() {
        return currentScene;
    }
}
