package use_case.saving;

import entity.Project;
import java.io.IOException;

public class SaveProjectInteractor implements SaveProjectInputBoundary {
    final SaveProjectDataAccessInterface saveProjectDataAccessObject;
    final SaveProjectOutputBoundary saveProjectPresenter;
    final Project project; // current project instance we are editing

    /**
     * Constructor: Initializes the Interactor.
     * * - saveProjectDataAccessObject: Used to write the project to disk.
     * - saveProjectPresenter: Used to display success or failure messages.
     * - project: The specific Project entity being saved.
     */
    public SaveProjectInteractor(SaveProjectDataAccessInterface saveProjectDataAccessObject,
                                 SaveProjectOutputBoundary saveProjectPresenter,
                                 Project project) {
        this.saveProjectDataAccessObject = saveProjectDataAccessObject;
        this.saveProjectPresenter = saveProjectPresenter;
        this.project = project;
    }

    /**
     * Executes the Save Project logic.
     *
     * 1. Attempts to save the current Project using the DAO.
     * 2. If successful, it creates OutputData and tells the Presenter to show the Success View.
     * 3. If saving fails (IOException), it catches the error and tells the Presenter to show the Fail View.
     */
    @Override
    public void execute(SaveProjectInputData saveProjectInputData) {
        try {
            // save the project using the Data Access Interface
            saveProjectDataAccessObject.save(project);

            // create Output Data
            SaveProjectOutputData saveProjectOutputData = new SaveProjectOutputData(
                    project.getName(),
                    "Project saved successfully.",
                    true
            );

            // tell the Presenter to update the view
            saveProjectPresenter.prepareSuccessView(saveProjectOutputData);

        } catch (IOException e) {
            // handle failure
            saveProjectPresenter.prepareFailView("Failed to save project: " + e.getMessage());
        }
    }
}