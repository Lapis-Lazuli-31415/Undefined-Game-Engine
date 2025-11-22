package entity;

import entity.scripting.environment.Environment;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({ "id", "name", "global_environment", "assets", "scenes" }) // force the order of keys in database.json
public class Project {

    private final String id;
    private final String name;
    private final ArrayList<Scene> scenes;
    private final AssetLib assets;
    private final GameController gameController;

    public Project(String id, String name, ArrayList<Scene> scenes, AssetLib assets, GameController gameController) {
        this.id = id;
        this.name = name;
        this.scenes = scenes;
        this.assets = assets;
        Environment globalEnvironment = new Environment();
        this.gameController = gameController;
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
    public AssetLib getAssets() {
        return assets;
    }

    @JsonIgnore // stops "game_controller" from appearing in the file
    public GameController getGameController() {
        return gameController;
    }

    @JsonProperty("global_environment") // creates the "global_environment" key
    public Environment getGlobalEnvironment() {
        if (gameController != null) {
            return gameController.getEnvironment();
        }
        return null;
    }


    public void play() {
        //
    }

    public void save(){
        //
    }
}
