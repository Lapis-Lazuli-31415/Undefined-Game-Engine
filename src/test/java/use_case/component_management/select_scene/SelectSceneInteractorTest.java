package use_case.component_management.select_scene;

import entity.Scene;
import org.junit.jupiter.api.Test;
import use_case.component_management.SceneRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SelectSceneInteractorTest {

    @Test
    void testSelectSceneSuccess() {
        // Arrange
        Scene scene = new Scene(UUID.randomUUID(), "Target Scene", new ArrayList<>());
        SceneRepository repository = new SceneRepository() {
            @Override
            public List<Scene> getAllScenes() {
                return null;
            }

            @Override
            public Scene getSceneByName(String name) {
                if ("Target Scene".equals(name)) {
                    return scene;
                }
                return null;
            }

            @Override
            public void saveScene(Scene scene) {

            }
        };

        final boolean[] wasCalled = {false};
        SelectSceneOutputBoundary presenter = new SelectSceneOutputBoundary() {
            @Override
            public void sceneSelected(Scene s) {
                wasCalled[0] = true;
                assertEquals(scene, s);
            }
        };

        SelectSceneInteractor interactor = new SelectSceneInteractor(repository, presenter);

        // Act
        interactor.selectScene("Target Scene");

        // Assert
        assertTrue(wasCalled[0]);
    }

    @Test
    void testSelectSceneNotFound() {
        // Arrange
        SceneRepository repository = new SceneRepository() {
            @Override
            public List<Scene> getAllScenes() {
                return null;
            }

            @Override
            public Scene getSceneByName(String name) {
                return null;
            }

            @Override
            public void saveScene(Scene scene) {

            }
        };

        SelectSceneOutputBoundary presenter = s -> fail("Should not be called");
        SelectSceneInteractor interactor = new SelectSceneInteractor(repository, presenter);

        // Act
        interactor.selectScene("Missing Scene");
    }
}
