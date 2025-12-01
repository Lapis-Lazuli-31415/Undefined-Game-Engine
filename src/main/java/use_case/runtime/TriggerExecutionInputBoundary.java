package use_case.runtime;

/**
 * Input Boundary for trigger execution use case.
 * Part of Use Case layer (pink ring in CA diagram).
 *
 * @author Wanru Cheng
 */
public interface TriggerExecutionInputBoundary {

    /**
     * Execute a trigger.
     *
     * @param inputData Input data containing trigger and environment info
     */
    void execute(TriggerExecutionInputData inputData);
}