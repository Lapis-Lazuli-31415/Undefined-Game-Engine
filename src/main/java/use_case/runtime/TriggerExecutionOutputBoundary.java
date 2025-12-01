package use_case.runtime;

/**
 * Output Boundary for trigger execution use case.
 * Part of Use Case layer (pink ring in CA diagram).
 *
 * @author Wanru Cheng
 */
public interface TriggerExecutionOutputBoundary {

    /**
     * Present successful trigger execution.
     *
     * @param outputData Output data with execution result
     */
    void presentSuccess(TriggerExecutionOutputData outputData);

    /**
     * Present trigger execution failure.
     *
     * @param errorMessage Error message
     */
    void presentError(String errorMessage);

    /**
     * Present that conditions were not met (trigger skipped).
     *
     * @param outputData Output data
     */
    void presentConditionsNotMet(TriggerExecutionOutputData outputData);
}