package use_case.create_scene;

import entity.Scene;

public interface CreateSceneUserDataAccessInterface {
    boolean existsByName(String name);
    void save(Scene scene);
}
