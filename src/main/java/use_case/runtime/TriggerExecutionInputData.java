package use_case.runtime;

import entity.GameObject;
import entity.scripting.Trigger;
import entity.scripting.environment.Environment;

/**
 * Input data for trigger execution use case.
 * Part of Use Case layer (pink ring in CA diagram).
 *
 * @author Wanru Cheng
 */
public class TriggerExecutionInputData {

    private final Trigger trigger;
    private final GameObject gameObject;
    private final Environment globalEnvironment;

    /**
     * Constructor.
     *
     * @param trigger The trigger to execute
     * @param gameObject The GameObject that owns the trigger
     * @param globalEnvironment The global environment
     */
    public TriggerExecutionInputData(Trigger trigger, GameObject gameObject, Environment globalEnvironment) {
        this.trigger = trigger;
        this.gameObject = gameObject;
        this.globalEnvironment = globalEnvironment;
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
}