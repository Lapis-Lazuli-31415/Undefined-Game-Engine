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

    public InMemorySceneRepository() {
        // Seed sample scenes & game objects
        Scene s1 = new Scene(UUID.randomUUID(), "Scene A", List.of(
                new GameObject("go-1", "Player", true, new ArrayList<>(), null),
                new GameObject("go-2", "Enemy", true, new ArrayList<>(), null)
        ));
        Scene s2 = new Scene(UUID.randomUUID(), "Scene B", List.of(
                new GameObject("go-3", "Tree", true, new ArrayList<>(), null)
        ));

        scenes.add(s1);
        scenes.add(s2);
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
}
