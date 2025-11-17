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
    // Add these methods to Scene.java

    /**
     * Get all GameObjects in the scene
     *
     * @return List of GameObjects
     */
    public ArrayList<GameObject> getGameObjects() {
        return new ArrayList<>(gameObjects);
    }

    /**
     * Get scene name
     *
     * @return Scene name
     */
    public String getName() {
        return name;
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
}
