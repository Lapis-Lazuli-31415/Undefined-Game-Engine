package data_access;

import data_access.saving.JsonProjectDataAccess;
import entity.*;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.action.WaitAction;
import entity.scripting.condition.NumericComparisonCondition;
import entity.scripting.environment.Assign;
import entity.scripting.environment.Environment;
import entity.scripting.event.OnClickEvent;
import entity.scripting.expression.value.NumericValue;
import entity.scripting.expression.variable.BooleanVariable;
import entity.scripting.expression.variable.NumericVariable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  Test ensuring the entire object graph is saved and loaded correctly via JSON.
 */
class JsonProjectDataAccessTest {

    @TempDir
    Path tempDir;

    private Project originalProject;
    private Project loadedProject;

    @BeforeEach
    void setUp() throws Exception {
        // Create a Complex Project
        originalProject = createComplexProject();

        // Define a temp file path
        File tempFile = tempDir.resolve("test_save.json").toFile();
        String tempPath = tempFile.getAbsolutePath();

        // SAVE IT
        // Using ObjectMapper directly to simulate the save since the DAO might have hardcoded paths
        // in the current implementation, or to ensure we are testing the serialization logic specifically.
        new com.fasterxml.jackson.databind.ObjectMapper()
                .enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT)
                .setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE)
                .writeValue(tempFile, originalProject);

        // LOAD IT BACK
        JsonProjectDataAccess dataAccess = new JsonProjectDataAccess();
        loadedProject = dataAccess.load(tempPath);
    }

    /**
     * Verifies that the top-level metadata of the project is correctly saved and loaded.
     * Checks if the Project ID and Project Name match the original values.
     */
    @Test
    void testProjectMetadata() {
        assertNotNull(loadedProject, "Loaded project should not be null");
        assertEquals(originalProject.getId(), loadedProject.getId());
        assertEquals(originalProject.getName(), loadedProject.getName());
    }

    /**
     * Tests the persistence of the Global Environment.
     * Checks if global variables (e.g., "score") defined in the project are correctly restored with their values.
     * @throws Exception if accessing variables from the environment fails.
     */
    @Test
    void testGlobalEnvironment() throws Exception {
        Environment globalEnv = loadedProject.getGlobalEnvironment();
        assertNotNull(globalEnv, "Global environment should exist");

        NumericVariable scoreVar = new NumericVariable("score", true);
        assertEquals(100.0, globalEnv.get(scoreVar), "Global variable 'score' should be 100.0");
    }

    /**
     * Verifies the structural integrity of Scenes and GameObjects.
     * Checks:
     * - The number of scenes.
     * - The name of the scene.
     * - The number of GameObjects within the scene.
     * - The name and active state of the GameObject.
     */
    @Test
    void testSceneAndGameObjectStructure() {
        assertEquals(1, loadedProject.getScenes().size(), "Should have 1 scene");
        Scene scene = loadedProject.getScenes().get(0);
        assertEquals("Level 1", scene.getName());

        assertEquals(1, scene.getGameObjects().size(), "Scene should have 1 game object");
        GameObject obj = scene.getGameObjects().get(0);
        assertEquals("Hero", obj.getName());
        assertTrue(obj.isActive());
    }

    /**
     * Tests the persistence of the Transform component.
     * Verifies that spatial data (X, Y, Rotation, Scale X, Scale Y) is correctly preserved
     * after the save/load cycle.
     */
    @Test
    void testGameObjectTransform() {
        GameObject obj = loadedProject.getScenes().get(0).getGameObjects().get(0);
        Transform t = obj.getTransform();

        assertNotNull(t, "Transform should not be null");
        assertEquals(10.0, t.getX());
        assertEquals(20.0, t.getY());
        assertEquals(45.0f, t.getRotation());
        assertEquals(1.0, t.getScaleX());
        assertEquals(1.0, t.getScaleY());
    }

    /**
     * Tests the persistence of the Local Environment attached to a specific GameObject.
     * Verifies that local variables (e.g., "health", "isDead") are correctly restored
     * with their respective types and values.
     * @throws Exception if accessing variables from the environment fails.
     */
    @Test
    void testGameObjectLocalEnvironment() throws Exception {
        GameObject obj = loadedProject.getScenes().get(0).getGameObjects().get(0);
        Environment localEnv = obj.getEnvironment();

        assertNotNull(localEnv, "Local environment should not be null");

        NumericVariable healthVar = new NumericVariable("health", false);
        assertEquals(50.0, localEnv.get(healthVar), "Local variable 'health' should be 50.0");

        BooleanVariable isDeadVar = new BooleanVariable("isDead", false);
        assertFalse(localEnv.get(isDeadVar), "Local variable 'isDead' should be false");
    }

    /**
     * Tests the persistence of the Scripting System (Triggers, Events, Conditions, Actions).
     * This is a deep verification that checks:
     * - The existence of the TriggerManager.
     * - The count of triggers.
     * - The type of Event (e.g., OnClickEvent).
     * - The Conditions (type and count).
     * - The Actions (type and count).
     * - The internal state of an Action (e.g., evaluating a WaitAction's expression) to ensure deep serialization worked.
     * @throws Exception if evaluation of expressions fails.
     */
    @Test
    void testTriggersEventsAndActions() throws Exception {
        GameObject obj = loadedProject.getScenes().get(0).getGameObjects().get(0);
        TriggerManager tm = obj.getTriggerManager();

        assertNotNull(tm, "TriggerManager should not be null");
        assertEquals(1, tm.getTriggers().size(), "Should have 1 trigger");

        Trigger trigger = tm.getTrigger(0);

        // Verify Event
        assertInstanceOf(OnClickEvent.class, trigger.getEvent(), "Event should be OnClickEvent");

        // Verify Condition
        assertEquals(1, trigger.getConditions().size());
        assertInstanceOf(NumericComparisonCondition.class, trigger.getConditions().get(0));

        // Verify Action
        assertEquals(1, trigger.getActions().size());
        assertInstanceOf(WaitAction.class, trigger.getActions().get(0));

        WaitAction wait = (WaitAction) trigger.getActions().get(0);
        // Evaluating the expression inside the action to ensure data persisted
        // We need the environments to evaluate
        Environment globalEnv = loadedProject.getGlobalEnvironment();
        Environment localEnv = obj.getEnvironment();
        assertEquals(1.5, wait.getSecondsExpression().evaluate(globalEnv, localEnv));
    }

    // --- Helper to build the project ---
    private Project createComplexProject() throws Exception {
        // --- Globals ---
        Environment globalEnv = new Environment();
        Assign.assign(globalEnv, new NumericVariable("score", true), 100.0);
        GameController controller = new GameController(globalEnv);

        // --- GameObject Local Data ---
        Environment localEnv = new Environment();
        Assign.assign(localEnv, new NumericVariable("health", false), 50.0);
        Assign.assign(localEnv, new BooleanVariable("isDead", false), false);

        // --- Transform ---
        Vector<Double> pos = new Vector<>(); pos.add(10.0); pos.add(20.0);
        Vector<Double> scale = new Vector<>(); scale.add(1.0); scale.add(1.0);
        Transform transform = new Transform(pos, 45.0f, scale);

        // --- Triggers ---
        TriggerManager tm = new TriggerManager();
        Trigger trigger = new Trigger(new OnClickEvent(), true);

        // Condition: 1 > 0
        NumericValue left = new NumericValue(1);
        NumericValue right = new NumericValue(0);
        trigger.addCondition(new NumericComparisonCondition(left, ">", right));

        // Action: Wait 1.5s
        trigger.addAction(new WaitAction(new NumericValue(1.5)));
        tm.addTrigger(trigger);

        // --- Assembly ---
        GameObject hero = new GameObject("obj-1", "Hero", true, new ArrayList<>(), localEnv);
        hero.setTransform(transform);
        hero.setTriggerManager(tm);

        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(hero);
        Scene scene = new Scene(UUID.randomUUID(), "Level 1", objects);

        ArrayList<Scene> scenes = new ArrayList<>();
        scenes.add(scene);

        return new Project("proj-1", "My RPG", scenes, new AssetLib(), controller);
    }
}