package saving;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test ensuring the entire object graph is saved and loaded correctly via JSON.
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
        // We configure the mapper to see PRIVATE fields (Visibility.ANY)
        ObjectMapper testMapper = new ObjectMapper();
        testMapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
        testMapper.setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE);
        testMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        testMapper.writeValue(tempFile, originalProject);

        // LOAD IT BACK
        JsonProjectDataAccess dataAccess = new JsonProjectDataAccess();

        // Use Reflection to configure the DAO's internal mapper
        Field mapperField = JsonProjectDataAccess.class.getDeclaredField("mapper");
        mapperField.setAccessible(true);
        ObjectMapper daoMapper = (ObjectMapper) mapperField.get(dataAccess);
        daoMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        loadedProject = dataAccess.load(tempPath);
    }

    @Test
    void testProjectMetadata() {
        assertNotNull(loadedProject, "Loaded project should not be null");
        assertEquals(originalProject.getId(), loadedProject.getId());
        assertEquals(originalProject.getName(), loadedProject.getName());
    }

    @Test
    void testGlobalEnvironment() throws Exception {
        Environment globalEnv = loadedProject.getGlobalEnvironment();
        assertNotNull(globalEnv, "Global environment should exist");

        NumericVariable scoreVar = new NumericVariable("score", true);
        assertEquals(100.0, globalEnv.get(scoreVar), "Global variable 'score' should be 100.0");
    }

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

    @Test
    void testTriggersEventsAndActions() throws Exception {
        GameObject obj = loadedProject.getScenes().get(0).getGameObjects().get(0);
        TriggerManager tm = obj.getTriggerManager();

        assertNotNull(tm, "TriggerManager should not be null");
        assertEquals(1, tm.getTriggers().size(), "Should have 1 trigger");

        Trigger trigger = tm.getTrigger(0);

        assertInstanceOf(OnClickEvent.class, trigger.getEvent(), "Event should be OnClickEvent");

        assertEquals(1, trigger.getConditions().size());
        assertInstanceOf(NumericComparisonCondition.class, trigger.getConditions().get(0));

        assertEquals(1, trigger.getActions().size());
        assertInstanceOf(WaitAction.class, trigger.getActions().get(0));

        WaitAction wait = (WaitAction) trigger.getActions().get(0);
        Environment globalEnv = loadedProject.getGlobalEnvironment();
        Environment localEnv = obj.getEnvironment();
        assertEquals(1.5, wait.getSecondsExpression().evaluate(globalEnv, localEnv));
    }

    // --- Helper to build the project ---
    private Project createComplexProject() throws Exception {
        Environment globalEnv = new Environment();
        Assign.assign(globalEnv, new NumericVariable("score", true), 100.0);
        GameController controller = new GameController(globalEnv);

        Environment localEnv = new Environment();
        Assign.assign(localEnv, new NumericVariable("health", false), 50.0);
        Assign.assign(localEnv, new BooleanVariable("isDead", false), false);

        Vector<Double> pos = new Vector<>(); pos.add(10.0); pos.add(20.0);
        Vector<Double> scale = new Vector<>(); scale.add(1.0); scale.add(1.0);
        Transform transform = new Transform(pos, 45.0f, scale);

        TriggerManager tm = new TriggerManager();
        Trigger trigger = new Trigger(new OnClickEvent(), true);

        NumericValue left = new NumericValue(1);
        NumericValue right = new NumericValue(0);
        trigger.addCondition(new NumericComparisonCondition(left, ">", right));

        trigger.addAction(new WaitAction(new NumericValue(1.5)));
        tm.addTrigger(trigger);

        // --- Assembly ---
        // FIX: Added 'null' as the last argument for SpriteRenderer
        GameObject hero = new GameObject("obj-1", "Hero", true, new ArrayList<>(), localEnv, null);
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