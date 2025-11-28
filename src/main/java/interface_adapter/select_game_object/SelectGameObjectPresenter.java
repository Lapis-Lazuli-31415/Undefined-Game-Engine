package interface_adapter.select_game_object;

import interface_adapter.EditorState;
import entity.Scene;
import entity.GameObject;
import use_case.component_management.select_game_object.SelectGameObjectOutputBoundary;

public class SelectGameObjectPresenter implements SelectGameObjectOutputBoundary {

    private final EditorState state;
    private final GameObjectSelectionListener listener;

    public SelectGameObjectPresenter(EditorState state,
                                         GameObjectSelectionListener listener) {
        this.state = state;
        this.listener = listener;
    }

    @Override
    public void gameObjectSelected(Scene scene, GameObject gameObject) {
        state.setCurrentScene(scene);           // ensure scene synced
        state.setCurrentGameObject(gameObject); // store actual entity
        listener.onGameObjectSelected(scene, gameObject);
    }

    public interface GameObjectSelectionListener {
        void onGameObjectSelected(Scene scene, GameObject gameObject);
    }
}
