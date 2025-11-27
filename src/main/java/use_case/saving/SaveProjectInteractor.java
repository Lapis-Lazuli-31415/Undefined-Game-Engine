package use_case.saving;

import entity.Project;
import java.io.IOException;

public class SaveProjectInteractor implements SaveProjectInputBoundary {
    final SaveProjectDataAccessInterface saveProjectDataAccessObject;
    final SaveProjectOutputBoundary saveProjectPresenter;
    final Project project; // current project instance we are editing

    public SaveProjectInteractor(SaveProjectDataAccessInterface saveProjectDataAccessObject,
                                 SaveProjectOutputBoundary saveProjectPresenter,
                                 Project project) {
        this.saveProjectDataAccessObject = saveProjectDataAccessObject;
        this.saveProjectPresenter = saveProjectPresenter;
        this.project = project;
    }

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