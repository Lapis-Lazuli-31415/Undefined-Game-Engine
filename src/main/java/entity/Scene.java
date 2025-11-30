package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// annotation prevents crashes if the JSON contains fields (like "game_object_names")
// that don't exist in the class anymore.
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scene {

    private final UUID id;
    private final String name;
    private List<GameObject> gameObjects;

    @JsonCreator
    public Scene(@JsonProperty("id") UUID id,
                 @JsonProperty("name") String name,
                 @JsonProperty("game_objects") List<GameObject> gameObjects) {
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


    // This tells Jackson: "Do not save this to JSON, and do not try to load it."
    @JsonIgnore
    public List<String> getGameObjectNames() {
        if (gameObjects == null) return List.of();
        return gameObjects.stream()
                .map(GameObject::getName)
                .toList();
    }

    public boolean hasGameObject(GameObject gameObject) {
        if (gameObject == null) {
            return false;
        }
        return gameObjects.contains(gameObject);
    }

    public GameObject getGameObjectByName(String name) {
        if (name == null) {
            return null;
        }

        for (GameObject gameObject : gameObjects) {
            if (gameObject.getName().equals(name)) {
                return gameObject;
            }
        }

        return null;
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
