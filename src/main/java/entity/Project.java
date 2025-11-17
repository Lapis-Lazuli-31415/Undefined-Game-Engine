package entity;

import entity.scripting.environment.Environment;

import java.util.ArrayList;

public class Project {

    private final String id;
    private final String name;
    private final ArrayList<Scene> scenes;
    private final ArrayList<Asset> assets;
    private final GameController gameController;

    public Project(String id, String name, ArrayList<Scene> scenes, ArrayList<Asset> assets, GameController gameController) {
        this.id = id;
        this.name = name;
        this.scenes = new ArrayList<>();
        this.assets = new ArrayList<>();
        Environment globalEnvironment = new Environment();
        this.gameController = new GameController(globalEnvironment);
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Scene> getScenes() {
        return scenes;
    }
    public ArrayList<Asset> getAssets() {
        return assets;
    }

    public GameController getGameController() {
        return gameController;
    }


    public void play() {
        //
    }

    public void save(){
        //
    }
}
