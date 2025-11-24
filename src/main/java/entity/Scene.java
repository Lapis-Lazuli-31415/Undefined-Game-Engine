package entity;

import java.util.ArrayList;
import java.util.UUID;

public class Scene {

    private final UUID id;
    private final String name;
    private ArrayList<GameObject> gameObjects;
    private Music backgroundMusic;

    public Scene(UUID id, String name, ArrayList<GameObject> gameObjects, Music backgroundMusic) {
        this.id = id;
        this.name = name;
        this.gameObjects = gameObjects;
        this.backgroundMusic = backgroundMusic;
    }

    public static Scene create(String name) {
        return new Scene(UUID.randomUUID(), name, new ArrayList<>(), new Music());
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
