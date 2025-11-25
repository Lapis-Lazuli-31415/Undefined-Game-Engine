package interface_adapter.trigger.create;

import use_case.trigger.create.ConditionCreateInputBoundary;

public class TriggerCreateController {
    private final ConditionCreateInputBoundary triggerCreateInteractor;

    public TriggerCreateController(ConditionCreateInputBoundary triggerCreateInteractor) {
        this.triggerCreateInteractor = triggerCreateInteractor;
    }

    public void execute() {
        triggerCreateInteractor.execute();
    }
}
