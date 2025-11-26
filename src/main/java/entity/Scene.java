package entity;

import java.util.ArrayList;

public class Scene {

    private final String id;
    private final String name;
    private ArrayList<GameObject> gameObjects;
    private Music backgroundMusic;

    public Scene(String id, String name, ArrayList<GameObject> gameObjects, Music backgroundMusic) {
        this.id = id;
        this.name = name;
        this.gameObjects = gameObjects;
        this.backgroundMusic = backgroundMusic;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    /**
     * Add a GameObject to the scene
     *
     * @param gameObject GameObject to add
     */
    public void addGameObject(GameObject gameObject) {
        if (!gameObjects.contains(gameObject)) {
            gameObjects.add(gameObject);
        }
    }

    /**
     * Remove a GameObject from the scene
     *
     * @param gameObject GameObject to remove
     */
    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    public Music getBackgroundMusic() { return backgroundMusic;

    }
}
}
