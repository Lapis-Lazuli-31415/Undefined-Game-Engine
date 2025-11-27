package interface_adapter.saving;

import use_case.saving.SaveProjectInputBoundary;
import use_case.saving.SaveProjectInputData;

public class SaveProjectController {

    private final SaveProjectInputBoundary saveProjectInteractor;

    public SaveProjectController(SaveProjectInputBoundary saveProjectInteractor) {
        this.saveProjectInteractor = saveProjectInteractor;
    }

    /**
     * Triggers the save operation.
     * @param nameToSet The name the user typed, or null if they just clicked "Save".
     */
    public void execute(String nameToSet) {
        SaveProjectInputData inputData = new SaveProjectInputData(nameToSet);
        saveProjectInteractor.execute(inputData);
    }
}