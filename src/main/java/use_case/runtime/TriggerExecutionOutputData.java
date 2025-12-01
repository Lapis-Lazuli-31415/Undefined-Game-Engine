package use_case.runtime;

/**
 * Output data for trigger execution use case.
 * Part of Use Case layer (pink ring in CA diagram).
 *
 * @author Wanru Cheng
 */
public class TriggerExecutionOutputData {

    private final String gameObjectName;
    private final String eventName;
    private final boolean executed;
    private final int actionsExecuted;

    /**
     * Constructor.
     *
     * @param gameObjectName Name of the GameObject
     * @param eventName Name of the event
     * @param executed Whether the trigger was executed
     * @param actionsExecuted Number of actions executed
     */
    public TriggerExecutionOutputData(String gameObjectName, String eventName, boolean executed, int actionsExecuted) {
        this.gameObjectName = gameObjectName;
        this.eventName = eventName;
        this.executed = executed;
        this.actionsExecuted = actionsExecuted;
    }

    public String getGameObjectName() {
        return gameObjectName;
    }

    public String getEventName() {
        return eventName;
    }

    public boolean isExecuted() {
        return executed;
    }

    public int getActionsExecuted() {
        return actionsExecuted;
    }
}