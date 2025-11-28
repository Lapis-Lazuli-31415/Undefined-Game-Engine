package interface_adapter.select_scene;

import interface_adapter.EditorState;
import entity.Scene;
import use_case.component_management.select_scene.SelectSceneOutputBoundary;

public class SelectScenePresenter implements SelectSceneOutputBoundary {

    private SceneSelectionListener listener;

    public SelectScenePresenter(
                                SceneSelectionListener listener) {
        this.listener = listener;
    }

    public void setListener(SceneSelectionListener listener) {
        this.listener = listener;
    }

    @Override
    public void sceneSelected(Scene scene) {
        EditorState.setCurrentScene(scene);   // store actual entity
        listener.onSceneChange(scene);
    }

    public interface SceneSelectionListener {
        void onSceneChange(Scene scene);
    }
}
