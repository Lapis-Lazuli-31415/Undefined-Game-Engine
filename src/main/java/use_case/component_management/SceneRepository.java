package use_case.component_management;

import entity.Scene;
import java.util.List;

public interface SceneRepository {
    List<Scene> getAllScenes();
    Scene getSceneByName(String name);
    void saveScene(Scene scene);
}
