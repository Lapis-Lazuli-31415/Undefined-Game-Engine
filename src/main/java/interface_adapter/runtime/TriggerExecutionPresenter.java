package interface_adapter.runtime;

import use_case.runtime.TriggerExecutionOutputBoundary;
import use_case.runtime.TriggerExecutionOutputData;

/**
 * Presenter for trigger execution.
 * Part of Interface Adapter layer (green ring in CA diagram).
 *
 * @author Wanru Cheng
 */
public class TriggerExecutionPresenter implements TriggerExecutionOutputBoundary {

    @Override
    public void presentSuccess(TriggerExecutionOutputData outputData) {
        System.out.println("Executed: " + outputData.getEventName() + " on " + outputData.getGameObjectName() +
                " (" + outputData.getActionsExecuted() + " actions)");
    }

    @Override
    public void presentError(String errorMessage) {
        System.err.println("Trigger error: " + errorMessage);
    }

    @Override
    public void presentConditionsNotMet(TriggerExecutionOutputData outputData) {
        // Silent - don't log when conditions aren't met (happens every frame for most triggers)
    }
}