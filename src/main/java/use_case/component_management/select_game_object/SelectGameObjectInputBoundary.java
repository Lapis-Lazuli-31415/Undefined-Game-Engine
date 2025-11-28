package use_case.component_management.select_game_object;

import entity.GameObject;

public interface SelectGameObjectInputBoundary {
    void selectGameObject(String sceneName, GameObject gameObjectName);
}
