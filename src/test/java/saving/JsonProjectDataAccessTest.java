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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Test ensuring the entire object graph is saved and loaded correctly via JSON.
 */
class JsonProjectDataAccessTest {

    @TempDir
    Path tempDir;

    @Test
    void testSaveAndLoadFullProjectIntegrity() throws Exception {
        // 1. Create a Complex Project (GameObjects, Triggers, Variables, Transforms)
        Project originalProject = createComplexProject();

        // Define a temp file path
        File tempFile = tempDir.resolve("test_save.json").toFile();
        String tempPath = tempFile.getAbsolutePath();

        // 2. SAVE IT
        JsonProjectDataAccess dataAccess = new JsonProjectDataAccess();
        // We override the hardcoded "database.json" in this test by calling ObjectMapper directly
        // OR we can assume save() writes to a file we can move, but since your DAO writes to "database.json"
        // strictly, we might need to modify the DAO to accept a path, or just write/read normally.
        // For this test, let's use the mapper inside the DAO to write to our temp file explicitly
        // to avoid cluttering your project root.

        // Reflection or Modification: Ideally, JsonProjectDataAccess should accept a filepath.
        // Assuming we use the public method load(path) and a custom save for testing:
        new com.fasterxml.jackson.databind.ObjectMapper()
                .enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT)
                .setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE)
                .writeValue(tempFile, originalProject);

        // 3. LOAD IT BACK
        Project loadedProject = dataAccess.load(tempPath);

        // 4. VERIFY INTEGRITY (Assertions)
        assertNotNull(loadedProject);
        assertEquals(originalProject.getId(), loadedProject.getId());
        assertEquals(originalProject.getName(), loadedProject.getName());

        // Check Global Environment
        Environment globalEnv = loadedProject.getGlobalEnvironment();
        assertNotNull(globalEnv);
        NumericVariable scoreVar = new NumericVariable("score", true);
        assertEquals(100.0, globalEnv.get(scoreVar));

        // Check Scene
        assertEquals(1, loadedProject.getScenes().size());
        Scene scene = loadedProject.getScenes().get(0);
        assertEquals("Level 1", scene.getName());

        // Check GameObject
        assertEquals(1, scene.getGameObjects().size());
        GameObject obj = scene.getGameObjects().get(0);
        assertEquals("Hero", obj.getName());
        assertTrue(obj.isActive());

        // Check Transform
        Transform t = obj.getTransform();
        assertEquals(10.0, t.getX());
        assertEquals(20.0, t.getY());
        assertEquals(45.0f, t.getRotation());

        // Check Local Environment (Variables)
        NumericVariable healthVar = new NumericVariable("health", false);
        assertEquals(50.0, obj.getEnvironment().get(healthVar));

        // Check Triggers (The hardest part!)
        TriggerManager tm = obj.getTriggerManager();
        assertNotNull(tm);
        assertEquals(1, tm.getTriggers().size());
        Trigger trigger = tm.getTrigger(0);

        // Verify Event
        assertInstanceOf(OnClickEvent.class, trigger.getEvent());

        // Verify Condition
        assertEquals(1, trigger.getConditions().size());
        assertInstanceOf(NumericComparisonCondition.class, trigger.getConditions().get(0));

        // Verify Action
        assertEquals(1, trigger.getActions().size());
        assertInstanceOf(WaitAction.class, trigger.getActions().get(0));
        WaitAction wait = (WaitAction) trigger.getActions().get(0);
        // Evaluating the expression inside the action to ensure data persisted
        assertEquals(1.5, wait.getSecondsExpression().evaluate(globalEnv, obj.getEnvironment()));
    }

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