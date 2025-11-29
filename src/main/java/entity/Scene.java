package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Scene {

    private final UUID id;
    private final String name;
    private List<GameObject> gameObjects;

    // The @JsonCreator annotation tells Jackson to use this constructor
    // instead of looking for a no-arg constructor.
    @JsonCreator
    public Scene(@JsonProperty("id") UUID id,
                 @JsonProperty("name") String name,
                 @JsonProperty("game_objects") List<GameObject> gameObjects) {
        this.id = id;
        this.name = name;
        // Handle null list just in case the JSON is missing the field
        this.gameObjects = gameObjects != null ? gameObjects : new ArrayList<>();
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

    public boolean hasGameObject(GameObject gameObject) {
        if (gameObject == null) {
            return false;
        }
        return gameObjects.contains(gameObject);
    }
}