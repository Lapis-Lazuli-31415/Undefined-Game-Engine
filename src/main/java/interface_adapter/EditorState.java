package interface_adapter;

import entities.Scene;
import entities.GameObject;

public class EditorState {

    private Scene currentScene;
    private GameObject currentGameObject;

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(Scene scene) {
        this.currentScene = scene;
        this.currentGameObject = null; // reset game object when scene changes
    }

    public GameObject getCurrentGameObject() {
        return currentGameObject;
    }

    public void setCurrentGameObject(GameObject gameObject) {
        this.currentGameObject = gameObject;
    }

    public boolean hasScene() {
        return currentScene != null;
    }

    public boolean hasGameObject() {
        return currentGameObject != null;
    }
}
