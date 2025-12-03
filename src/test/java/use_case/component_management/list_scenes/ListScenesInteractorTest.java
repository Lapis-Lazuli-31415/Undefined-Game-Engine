package use_case.component_management.list_scenes;

import entity.GameObject;
import entity.Scene;
import org.junit.jupiter.api.Test;
import use_case.component_management.SceneRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ListScenesInteractorTest {

    @Test
    void testListScenesWithNoScenes() {
        // Arrange
        SceneRepository repository = new SceneRepository() {
            @Override
            public List<Scene> getAllScenes() {
                return new ArrayList<>();
            }
            @Override
            public Scene getSceneByName(String name) { return null; }
            @Override
            public void saveScene(Scene scene) {}
        };

        final boolean[] wasCalled = {false};
        ListScenesOutputBoundary presenter = sceneData -> {
            wasCalled[0] = true;
            assertTrue(sceneData.isEmpty());
        };

        ListScenesInteractor interactor = new ListScenesInteractor(repository, presenter);

        // Act
        interactor.listScenes();

        // Assert
        assertTrue(wasCalled[0]);
    }

    @Test
    void testListScenesWithScenesAndObjects() {
        // Arrange
        Scene scene1 = new Scene(UUID.randomUUID(), "Scene 1", new ArrayList<>());
        scene1.addGameObject(new GameObject("go1", "Player", true, null, null, null, null));

        Scene scene2 = new Scene(UUID.randomUUID(), "Scene 2", new ArrayList<>());
        // Empty scene

        List<Scene> scenes = List.of(scene1, scene2);

        SceneRepository repository = new SceneRepository() {
            @Override
            public List<Scene> getAllScenes() {
                return scenes;
            }
            @Override
            public Scene getSceneByName(String name) { return null; }
            @Override
            public void saveScene(Scene scene) {}
        };

        final boolean[] wasCalled = {false};
        ListScenesOutputBoundary presenter = sceneData -> {
            wasCalled[0] = true;
            assertEquals(2, sceneData.size());
            assertTrue(sceneData.containsKey("Scene 1"));
            assertTrue(sceneData.containsKey("Scene 2"));

            assertEquals(1, sceneData.get("Scene 1").size());
            assertEquals("Player", sceneData.get("Scene 1").get(0));
            assertTrue(sceneData.get("Scene 2").isEmpty());
        };

        ListScenesInteractor interactor = new ListScenesInteractor(repository, presenter);

        // Act
        interactor.listScenes();

        // Assert
        assertTrue(wasCalled[0]);
    }
}
