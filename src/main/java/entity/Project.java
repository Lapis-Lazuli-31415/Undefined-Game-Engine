package entity;

import entity.scripting.environment.Environment;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

// force the order of keys in database.json
@JsonPropertyOrder({ "id", "name", "global_environment", "assets", "scenes" })
public class Project {

    // CHANGE 1: REMOVED 'final'.
    // Jackson cannot update final variables after creating the object.
    private String id;
    private String name;
    private ArrayList<Scene> scenes;
    private AssetLib assets;
    private GameController gameController;

    public Project(String id, String name, ArrayList<Scene> scenes, AssetLib assets, GameController gameController) {
        this.id = id;
        this.name = name;
        this.scenes = scenes;
        this.assets = assets;
        this.gameController = gameController;
    }

    public Project() {
        this.id = "TEMP_ID";
        this.name = "Unknown";
        this.scenes = new ArrayList<>();
        this.assets = new AssetLib();

        this.gameController = new GameController(new Environment());
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public ArrayList<Scene> getScenes() { return scenes; }
    public AssetLib getAssets() { return assets; }

    @JsonIgnore
    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @JsonProperty("global_environment")
    public Environment getGlobalEnvironment() {
        if (gameController != null) {
            return gameController.getEnvironment();
        }
        return null;
    }

    // Jackson calls this when loading. It passes the 'env' from the file.
    @JsonProperty("global_environment")
    public void setGlobalEnvironment(Environment env) {

        if (this.gameController == null) {
            this.gameController = new GameController(env);
        } else {
            // Assuming GameController has a setEnvironment method.
            // If not, you might need to add one to GameController.java!
            this.gameController.setEnvironment(env);
        }
    }

    public void play() {}
    public void save() {}
}