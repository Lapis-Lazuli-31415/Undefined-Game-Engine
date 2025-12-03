package use_case.component_management.create_scene;

import entity.Scene;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateSceneInteractorTest {

    @Test
    void testCreateSceneSuccess() {
        // Arrange
        CreateSceneInputData inputData = new CreateSceneInputData("New Scene");

        CreateSceneUserDataAccessInterface dao = new CreateSceneUserDataAccessInterface() {
            @Override
            public boolean existsByName(String name) {
                return false;
            }

            @Override
            public void save(Scene scene) {
                assertEquals("New Scene", scene.getName());
            }
        };

        final boolean[] successCalled = {false};
        CreateSceneOutputBoundary presenter = new CreateSceneOutputBoundary() {
            @Override
            public void prepareSuccessView(CreateSceneOutputData outputData) {
                successCalled[0] = true;
                assertEquals("New Scene", outputData.getName());
                assertNotNull(outputData.getId());
            }

            @Override
            public void prepareFailureView(String error) {
                fail("Should not fail");
            }
        };

        CreateSceneInteractor interactor = new CreateSceneInteractor(dao, presenter);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(successCalled[0]);
    }

    @Test
    void testCreateSceneNameEmpty() {
        // Arrange
        CreateSceneInputData inputData = new CreateSceneInputData("");

        CreateSceneUserDataAccessInterface dao = new CreateSceneUserDataAccessInterface() {
            @Override
            public boolean existsByName(String name) {
                return false;
            }

            @Override
            public void save(Scene scene) {
                fail("Should not save");
            }
        };

        final boolean[] failCalled = {false};
        CreateSceneOutputBoundary presenter = new CreateSceneOutputBoundary() {
            @Override
            public void prepareSuccessView(CreateSceneOutputData outputData) {
                fail("Should not succeed");
            }

            @Override
            public void prepareFailureView(String error) {
                failCalled[0] = true;
                assertEquals("Scene name cannot be empty.", error);
            }
        };

        CreateSceneInteractor interactor = new CreateSceneInteractor(dao, presenter);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(failCalled[0]);
    }

    @Test
    void testCreateSceneNameNull() {
        // Arrange
        CreateSceneInputData inputData = new CreateSceneInputData(null);

        CreateSceneUserDataAccessInterface dao = new CreateSceneUserDataAccessInterface() {
            @Override
            public boolean existsByName(String name) {
                return false;
            }
            @Override
            public void save(Scene scene) {}
        };

        final boolean[] failCalled = {false};
        CreateSceneOutputBoundary presenter = new CreateSceneOutputBoundary() {
            @Override
            public void prepareSuccessView(CreateSceneOutputData outputData) {}

            @Override
            public void prepareFailureView(String error) {
                failCalled[0] = true;
                assertEquals("Scene name cannot be empty.", error);
            }
        };

        CreateSceneInteractor interactor = new CreateSceneInteractor(dao, presenter);
        interactor.execute(inputData);
        assertTrue(failCalled[0]);
    }

    @Test
    void testCreateSceneAlreadyExists() {
        // Arrange
        CreateSceneInputData inputData = new CreateSceneInputData("Existing Scene");

        CreateSceneUserDataAccessInterface dao = new CreateSceneUserDataAccessInterface() {
            @Override
            public boolean existsByName(String name) {
                return "Existing Scene".equals(name);
            }

            @Override
            public void save(Scene scene) {
                fail("Should not save");
            }
        };

        final boolean[] failCalled = {false};
        CreateSceneOutputBoundary presenter = new CreateSceneOutputBoundary() {
            @Override
            public void prepareSuccessView(CreateSceneOutputData outputData) {
                fail("Should not succeed");
            }

            @Override
            public void prepareFailureView(String error) {
                failCalled[0] = true;
                assertEquals("Scene with this name already exists.", error);
            }
        };

        CreateSceneInteractor interactor = new CreateSceneInteractor(dao, presenter);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(failCalled[0]);
    }
}
