package interface_adapter;

import entity.Scene;
import entity.GameObject;

/**
 * Global editor state management.
 * Utility class with static methods only - not meant to be instantiated.
 *
 * @author Wanru Cheng
 */
public class EditorState {

    private static Scene currentScene;
    private static GameObject currentGameObject;

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with static methods only.
     */
    private EditorState() {
        throw new UnsupportedOperationException("EditorState is a utility class and cannot be instantiated");
    }

    // --- Scene getters/setters ---
    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static void setCurrentScene(Scene scene) {
        currentScene = scene;

        // Optional: clear selected game object if it doesn't belong to the new scene
        if (currentGameObject != null && (scene == null || !scene.hasGameObject(currentGameObject))) {
            currentGameObject = null;
        }
    }

    // --- GameObject getters/setters ---
    public static GameObject getCurrentGameObject() {
        return currentGameObject;
    }

    public static void setCurrentGameObject(GameObject gameObject) {
        currentGameObject = gameObject;
    }

    // --- Convenience reset method ---
    public static void reset() {
        currentScene = null;
        currentGameObject = null;
    }
}