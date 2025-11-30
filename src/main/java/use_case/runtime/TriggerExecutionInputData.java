package use_case.runtime;

import entity.Scene;
import entity.GameObject;
import entity.scripting.Trigger;
import entity.scripting.environment.Environment;

/**
 * Input data for trigger execution use case.
 */
public class TriggerExecutionInputData {

    private final Trigger trigger;
    private final GameObject gameObject;
    private final Environment globalEnvironment;
    private final Scene scene;  // ✅ 添加 scene 字段

    /**
     * Constructor.
     *
     * @param trigger The trigger to execute
     * @param gameObject The game object that triggered the event
     * @param globalEnvironment The global environment
     * @param scene The current scene  ✅ 添加 scene 参数
     */
    public TriggerExecutionInputData(Trigger trigger, GameObject gameObject,
                                     Environment globalEnvironment, Scene scene) {
        this.trigger = trigger;
        this.gameObject = gameObject;
        this.globalEnvironment = globalEnvironment;
        this.scene = scene;  // ✅ 保存 scene
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public Environment getGlobalEnvironment() {
        return globalEnvironment;
    }

    public Scene getScene() {  // ✅ 添加 getter
        return scene;
    }
}