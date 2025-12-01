package interface_adapter.add_scene;

import entity.Scene;
import use_case.component_management.add_scene.AddSceneOutputBoundary;

public class AddScenePresenter implements AddSceneOutputBoundary {

    private SceneCreationListener listener;

    @Override
    public void sceneCreated(Scene scene) {
        if (listener != null)
            listener.onSceneCreated(scene);
    }

    public void setListener(SceneCreationListener listener) {
        this.listener = listener;
    }

    public interface SceneCreationListener {
        void onSceneCreated(Scene scene);
    }
}
