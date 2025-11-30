package saving;

import entity.AssetLib;
import entity.GameController;
import entity.Project;
import entity.Scene;
import entity.scripting.environment.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.saving.*;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SaveProjectInteractorTest {

    private MockDataAccess dataAccess;
    private MockPresenter presenter;
    private Project project;

    @BeforeEach
    void setUp() {
        // 1. Setup minimal dummy data
        Environment globalEnv = new Environment();
        GameController controller = new GameController(globalEnv);
        AssetLib assets = new AssetLib();
        ArrayList<Scene> scenes = new ArrayList<>();

        project = new Project("test-id", "Test Project", scenes, assets, controller);

        // 2. Init Mocks
        dataAccess = new MockDataAccess();
        presenter = new MockPresenter();
    }

    /**
     * Tests the successful execution of the save use case.
     * This test simulates a standard scenario where the user requests to save the project,
     * and the file system operation succeeds.
     * Expected Behavior:
     * - The DAO's save() method is called exactly once with the correct project.
     * - The Presenter's prepareSuccessView() method is called.
     * - The Output Data passed to the presenter indicates success and contains the correct project name.
     */
    @Test
    void testSaveProjectSuccess() {
        // Arrange
        SaveProjectInteractor interactor = new SaveProjectInteractor(dataAccess, presenter, project);
        SaveProjectInputData input = new SaveProjectInputData("New Name");

        // Act
        interactor.execute(input);

        // Assert
        assertTrue(dataAccess.saveCalled, "DataAccess save() should be called");
        assertEquals(project, dataAccess.savedProject, " The correct project should be passed to DataAccess");

        assertTrue(presenter.successViewCalled, "Presenter success view should be prepared");
        assertNotNull(presenter.successData, "Output data should not be null");
        assertTrue(presenter.successData.isSuccessful());
        assertEquals("Test Project", presenter.successData.getProjectName());
    }

    /**
     * Tests the success path when the input name is null (since the Interactor uses the entity's name).
     */
    @Test
    void testSaveProjectSuccess_NullInputName() {
        // Arrange
        SaveProjectInteractor interactor = new SaveProjectInteractor(dataAccess, presenter, project);
        // Project name is "Test Project" from setUp()
        SaveProjectInputData input = new SaveProjectInputData(null); // Explicitly null input name

        // Act
        interactor.execute(input);

        // Assert
        assertTrue(dataAccess.saveCalled, "DataAccess save() should be called");
        assertTrue(presenter.successViewCalled, "Presenter success view should be prepared");
        assertNotNull(presenter.successData, "Output data should not be null");
        assertTrue(presenter.successData.isSuccessful());
        // Verify output uses the Project entity's name, not the null input name
        assertEquals("Test Project", presenter.successData.getProjectName());
        assertTrue(presenter.successData.getMessage().contains("successfully"));
    }

    /**
     * Tests the failure scenario where saving fails due to an I/O error.
     * This test simulates a condition like a "Disk Full" or "Permission Denied" error
     * by forcing the mock DAO to throw an IOException.
     * Expected Behavior:
     * - The DAO's save() method is called.
     * - The Presenter's prepareFailView() method is called.
     * - The Presenter's prepareSuccessView() method is NOT called.
     * - The error message passed to the presenter contains the exception message (e.g., "Disk Full").
     */
    @Test
    void testSaveProjectFailure() {
        // Arrange
        dataAccess.shouldThrowError = true; // Simulate Disk Error
        SaveProjectInteractor interactor = new SaveProjectInteractor(dataAccess, presenter, project);
        SaveProjectInputData input = new SaveProjectInputData(null);

        // Act
        interactor.execute(input);

        // Assert
        assertTrue(dataAccess.saveCalled);
        assertFalse(presenter.successViewCalled, "Success view should NOT be called on error");
        assertTrue(presenter.failViewCalled, "Fail view SHOULD be called on error");
        assertTrue(presenter.errorMessage.contains("Disk Full"), "Error message should be passed to presenter");
    }

    // Mocks

    static class MockDataAccess implements SaveProjectDataAccessInterface {
        boolean saveCalled = false;
        Project savedProject;
        boolean shouldThrowError = false;

        @Override
        public void save(Project project) throws IOException {
            saveCalled = true;
            savedProject = project;
            if (shouldThrowError) {
                throw new IOException("Disk Full");
            }
        }
    }

    static class MockPresenter implements SaveProjectOutputBoundary {
        boolean successViewCalled = false;
        SaveProjectOutputData successData;

        boolean failViewCalled = false;
        String errorMessage;

        @Override
        public void prepareSuccessView(SaveProjectOutputData outputData) {
            successViewCalled = true;
            successData = outputData;
        }

        @Override
        public void prepareFailView(String error) {
            failViewCalled = true;
            errorMessage = error;
        }
    }
}