package use_case.component_management.select_game_object;

import entity.GameObject;
import entity.Scene;

public interface SelectGameObjectOutputBoundary {
    void gameObjectSelected(Scene scene, GameObject gameObject);
}
