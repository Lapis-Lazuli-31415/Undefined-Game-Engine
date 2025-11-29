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

    // --- Mocks ---

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