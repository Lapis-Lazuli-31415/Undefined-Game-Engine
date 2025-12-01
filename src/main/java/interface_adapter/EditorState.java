package interface_adapter;

import entity.Scene;
import entity.GameObject;
import use_case.component_management.SceneRepository;

/**
 * Global editor state management.
 * Utility class with static methods only - not meant to be instantiated.
 *
 * @author Wanru Cheng
 */
public class EditorState {

    private static String currentSceneName;
    private static GameObject currentGameObject;
    private static SceneRepository sceneRepository;

    public static void init(SceneRepository repository) {
        sceneRepository = repository;
    }

    public static String getCurrentSceneName() {
        return currentSceneName;
    }

    public static void setCurrentSceneName(String name) {
        currentSceneName = name;
    }

    public static Scene getCurrentScene() {
        return sceneRepository.getSceneByName(currentSceneName);
    }

    public static SceneRepository getSceneRepository() {
        return sceneRepository;
    }

    // --- GameObject getters/setters ---
    public static GameObject getCurrentGameObject() {
        return currentGameObject;
    }

    public static void setCurrentGameObject(GameObject gameObject) {
        currentGameObject = gameObject;
    }
}

