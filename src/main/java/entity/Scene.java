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

    // ADD @JsonIgnore HERE
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


}
