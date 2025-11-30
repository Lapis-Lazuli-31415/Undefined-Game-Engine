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
        if (gameObjects == null) {
            gameObjects = new ArrayList<>();
        }
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

    public void addGameObject(GameObject gameObject) {
        if (gameObjects == null) {
            gameObjects = new ArrayList<>();
        }
        gameObjects.add(gameObject);
    }



}
