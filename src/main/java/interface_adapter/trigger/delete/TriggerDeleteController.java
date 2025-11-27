package interface_adapter.trigger.delete;

import use_case.trigger.delete.TriggerDeleteInputBoundary;
import use_case.trigger.delete.TriggerDeleteInputData;

public class TriggerDeleteController {
    private final TriggerDeleteInputBoundary triggerDeleteInteractor;

    public TriggerDeleteController(TriggerDeleteInputBoundary triggerDeleteInteractor) {
        this.triggerDeleteInteractor = triggerDeleteInteractor;
    }

    public void execute(int index) {
        TriggerDeleteInputData inputData = new TriggerDeleteInputData(index);

        triggerDeleteInteractor.execute(inputData);
    }
}
