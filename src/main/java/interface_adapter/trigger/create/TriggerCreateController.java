package interface_adapter.trigger.create;

import use_case.trigger.create.TriggerCreateInputBoundary;

public class TriggerCreateController {
    private final TriggerCreateInputBoundary triggerCreateInteractor;

    public TriggerCreateController(TriggerCreateInputBoundary triggerCreateInteractor) {
        this.triggerCreateInteractor = triggerCreateInteractor;
    }

    public void execute() {
        triggerCreateInteractor.execute();
    }
}
