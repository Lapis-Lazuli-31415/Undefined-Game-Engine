package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Scene {

    private final UUID id;
    private final String name;
    private List<GameObject> gameObjects;

    public Scene(UUID id, String name, List<GameObject> gameObjects) {
        this.id = id;
        this.name = name;
        this.gameObjects = gameObjects;
    }

    public static Scene create(String name) {
        return new Scene(UUID.randomUUID(), name, new ArrayList<>());
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public List<String> getGameObjectNames() {
        if (gameObjects == null) return List.of();
        return gameObjects.stream()
                .map(GameObject::getName)
                .toList();
    }

    public boolean hasGameObject(String name) {
        if (gameObjects == null) return false;
        return gameObjects.stream()
                .anyMatch(go -> go.getName().equals(name));
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
// --- Copy method for preview isolation ---

    /**
     * Create a deep copy of this Scene.
     *
     * @return A new Scene with copied GameObjects
     */
    public Scene copy() {
        List<GameObject> copiedObjects = new ArrayList<>();

        if (this.gameObjects != null) {
            for (GameObject obj : this.gameObjects) {
                copiedObjects.add(obj.copy());
            }
        }

        return new Scene(this.id, this.name, copiedObjects);
    }
}
