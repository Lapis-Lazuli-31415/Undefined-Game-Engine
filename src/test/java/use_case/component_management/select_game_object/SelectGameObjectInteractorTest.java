package use_case.component_management.select_game_object;

import entity.GameObject;
import entity.Scene;
import org.junit.jupiter.api.Test;
import use_case.component_management.SceneRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SelectGameObjectInteractorTest {

    @Test
    void testSelectGameObjectSuccess() {
        // Arrange
        GameObject gameObject = new GameObject("go1", "Test Object", true, null, null, null, null);
        Scene scene = new Scene(UUID.randomUUID(), "Test Scene", new ArrayList<>());
        scene.addGameObject(gameObject);

        SceneRepository repository = new SceneRepository() {
            @Override
            public List<Scene> getAllScenes() {
                return null;
            }

            @Override
            public Scene getSceneByName(String name) {
                if ("Test Scene".equals(name)) {
                    return scene;
                }
                return null;
            }

            @Override
            public void saveScene(Scene scene) {
            }
        };

        // Mutable boolean to verify call
        final boolean[] wasCalled = {false};
        SelectGameObjectOutputBoundary presenter = new SelectGameObjectOutputBoundary() {
            @Override
            public void gameObjectSelected(Scene s, GameObject go) {
                wasCalled[0] = true;
                assertEquals(scene, s);
                assertEquals(gameObject, go);
            }
        };

        SelectGameObjectInteractor interactor = new SelectGameObjectInteractor(repository, presenter);

        // Act
        interactor.selectGameObject("Test Scene", gameObject);

        // Assert
        assertTrue(wasCalled[0], "Presenter should be called when scene and game object exist");
    }

    @Test
    void testSelectGameObjectSceneNotFound() {
        // Arrange
        GameObject gameObject = new GameObject("go1");
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

        SelectGameObjectOutputBoundary presenter = (s, go) -> fail("Presenter should not be called");
        SelectGameObjectInteractor interactor = new SelectGameObjectInteractor(repository, presenter);

        // Act
        interactor.selectGameObject("NonExistentScene", gameObject);
    }

    @Test
    void testSelectGameObjectObjectNotFoundInScene() {
        // Arrange
        GameObject gameObject = new GameObject("go1", "Test Object", true, null, null, null, null);
        Scene scene = new Scene(UUID.randomUUID(), "Test Scene", new ArrayList<>());
        // Don't add gameObject to scene

        SceneRepository repository = new SceneRepository() {
            @Override
            public List<Scene> getAllScenes() {
                return null;
            }

            @Override
            public Scene getSceneByName(String name) {
                return scene;
            }

            @Override
            public void saveScene(Scene scene) {
            }
        };

        SelectGameObjectOutputBoundary presenter = (s, go) -> fail("Presenter should not be called if game object is not in scene");
        SelectGameObjectInteractor interactor = new SelectGameObjectInteractor(repository, presenter);

        // Act
        interactor.selectGameObject("Test Scene", gameObject);
    }
}
