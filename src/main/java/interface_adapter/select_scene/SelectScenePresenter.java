package interface_adapter.select_scene;

import interface_adapter.EditorState;
import entity.Scene;
import use_case.component_management.select_scene.SelectSceneOutputBoundary;

public class SelectScenePresenter implements SelectSceneOutputBoundary {

    private final EditorState state;
    private final SceneSelectionListener listener;

    public SelectScenePresenter(EditorState state,
                                SceneSelectionListener listener) {
        this.state = state;
        this.listener = listener;
    }

    @Override
    public void sceneSelected(Scene scene) {
        state.setCurrentScene(scene);   // store actual entity
        listener.onSceneChange(scene);
    }

    public interface SceneSelectionListener {
        void onSceneChange(Scene scene);
    }
}
