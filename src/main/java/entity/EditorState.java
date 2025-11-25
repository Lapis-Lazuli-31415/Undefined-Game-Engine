package entity;

public class EditorState {
    private Scene currentScene;
    private GameObject currentGameObject;

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(Scene scene) {
        this.currentScene = scene;
    }

    public GameObject getCurrentGameObject() {
        return currentGameObject;
    }

    public void setCurrentGameObject(GameObject gameObject) {
        this.currentGameObject = gameObject;
    }
}
