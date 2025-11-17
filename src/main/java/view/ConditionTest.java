package view;

import entity.GameObject;
import entity.Scene;
import entity.Transform;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.event.OnKeyPressEvent;
import entity.scripting.action.Action;
import entity.scripting.condition.Condition;
import entity.scripting.environment.Environment;
import entity.scripting.environment.EnvironmentHelper;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Test with Conditions - Using correct Environment API
 */
public class ConditionTest {

    private static final String VAR_TYPE = "game";

    public static void main(String[] args) {
        Scene scene = createTestScene();
        Environment globalEnv = new Environment();

        // Set global variables using correct API
        EnvironmentHelper.setInt(globalEnv, VAR_TYPE, "playerHealth", 100);
        EnvironmentHelper.setBoolean(globalEnv, VAR_TYPE, "hasKey", false);

        PreviewWindow window = new PreviewWindow(scene, globalEnv);
        window.display();

        System.out.println("\n=== Condition Test ===");
        System.out.println("Global variables:");
        System.out.println("  playerHealth = 100");
        System.out.println("  hasKey = false");
        System.out.println("\nTry pressing:");
        System.out.println("  W - Works only if playerHealth > 50");
        System.out.println("  E - Works only if hasKey == true");
        System.out.println("  H - Heal (increase playerHealth)");
        System.out.println("  K - Get key (set hasKey = true)");
        System.out.println("  D - Take damage (decrease playerHealth)");
        System.out.println("  I - Show info\n");
    }

    private static Scene createTestScene() {
        ArrayList<GameObject> objects = new ArrayList<>();

        // Move forward - requires health > 50
        objects.add(createConditionalMove("MoveForward", "W", 50, 50));

        // Open door - requires key
        objects.add(createConditionalAction("OpenDoor", "E", 50, 110));

        // Heal - always works
        objects.add(createHealAction("Heal", "H", 50, 170));

        // Get key - always works
        objects.add(createGetKeyAction("GetKey", "K", 50, 230));

        // Take damage
        objects.add(createDamageAction("TakeDamage", "D", 50, 290));

        // Show info
        objects.add(createShowInfoAction("ShowInfo", "I", 50, 350));

        return new Scene("condition-test", "Condition Test", objects, null);
    }

    /**
     * Action that requires health > 50
     */
    private static GameObject createConditionalMove(String name, String key, double x, double y) {
        GameObject obj = new GameObject(
                name, name, true,
                new ArrayList<>(),
                new Environment()
        );

        setPosition(obj, x, y);

        TriggerManager tm = obj.getTriggerManager();
        Trigger trigger = new Trigger(new OnKeyPressEvent(key));

        // Add condition: playerHealth > 50
        trigger.addCondition(new Condition() {
            @Override
            public boolean evaluate(Environment globalEnv, Environment localEnv) {
                int health = EnvironmentHelper.getInt(globalEnv, VAR_TYPE, "playerHealth", 0);
                return health > 50;
            }
        });

        // Add action
        trigger.addAction(new Action() {
            @Override
            public void execute(Environment globalEnv, Environment localEnv) {
                System.out.println("✓ Moving forward! (health > 50)");
            }
        });

        tm.getAllTriggers().add(trigger);

        return obj;
    }

    /**
     * Action that requires key
     */
    private static GameObject createConditionalAction(String name, String key, double x, double y) {
        GameObject obj = new GameObject(
                name, name, true,
                new ArrayList<>(),
                new Environment()
        );

        setPosition(obj, x, y);

        TriggerManager tm = obj.getTriggerManager();
        Trigger trigger = new Trigger(new OnKeyPressEvent(key));

        // Add condition: hasKey == true
        trigger.addCondition(new Condition() {
            @Override
            public boolean evaluate(Environment globalEnv, Environment localEnv) {
                return EnvironmentHelper.getBoolean(globalEnv, VAR_TYPE, "hasKey", false);
            }
        });

        // Add action
        trigger.addAction(new Action() {
            @Override
            public void execute(Environment globalEnv, Environment localEnv) {
                System.out.println("✓ Door opened! (hasKey == true)");
            }
        });

        tm.getAllTriggers().add(trigger);

        return obj;
    }

    /**
     * Heal action - increases playerHealth
     */
    private static GameObject createHealAction(String name, String key, double x, double y) {
        GameObject obj = new GameObject(
                name, name, true,
                new ArrayList<>(),
                new Environment()
        );

        setPosition(obj, x, y);

        TriggerManager tm = obj.getTriggerManager();
        Trigger trigger = new Trigger(new OnKeyPressEvent(key));

        trigger.addAction(new Action() {
            @Override
            public void execute(Environment globalEnv, Environment localEnv) {
                int health = EnvironmentHelper.getInt(globalEnv, VAR_TYPE, "playerHealth", 0);
                int newHealth = Math.min(health + 20, 100);
                EnvironmentHelper.setInt(globalEnv, VAR_TYPE, "playerHealth", newHealth);
                System.out.println("💊 Healed! Health: " + newHealth);
            }
        });

        tm.getAllTriggers().add(trigger);

        return obj;
    }

    /**
     * Take damage action
     */
    private static GameObject createDamageAction(String name, String key, double x, double y) {
        GameObject obj = new GameObject(
                name, name, true,
                new ArrayList<>(),
                new Environment()
        );

        setPosition(obj, x, y);

        TriggerManager tm = obj.getTriggerManager();
        Trigger trigger = new Trigger(new OnKeyPressEvent(key));

        trigger.addAction(new Action() {
            @Override
            public void execute(Environment globalEnv, Environment localEnv) {
                int health = EnvironmentHelper.getInt(globalEnv, VAR_TYPE, "playerHealth", 0);
                int newHealth = Math.max(health - 20, 0);
                EnvironmentHelper.setInt(globalEnv, VAR_TYPE, "playerHealth", newHealth);
                System.out.println("💔 Took damage! Health: " + newHealth);
            }
        });

        tm.getAllTriggers().add(trigger);

        return obj;
    }

    /**
     * Get key action
     */
    private static GameObject createGetKeyAction(String name, String key, double x, double y) {
        GameObject obj = new GameObject(
                name, name, true,
                new ArrayList<>(),
                new Environment()
        );

        setPosition(obj, x, y);

        TriggerManager tm = obj.getTriggerManager();
        Trigger trigger = new Trigger(new OnKeyPressEvent(key));

        trigger.addAction(new Action() {
            @Override
            public void execute(Environment globalEnv, Environment localEnv) {
                EnvironmentHelper.setBoolean(globalEnv, VAR_TYPE, "hasKey", true);
                System.out.println("🔑 Got the key! Can now open doors.");
            }
        });

        tm.getAllTriggers().add(trigger);

        return obj;
    }

    /**
     * Show info action
     */
    private static GameObject createShowInfoAction(String name, String key, double x, double y) {
        GameObject obj = new GameObject(
                name, name, true,
                new ArrayList<>(),
                new Environment()
        );

        setPosition(obj, x, y);

        TriggerManager tm = obj.getTriggerManager();
        Trigger trigger = new Trigger(new OnKeyPressEvent(key));

        trigger.addAction(new Action() {
            @Override
            public void execute(Environment globalEnv, Environment localEnv) {
                int health = EnvironmentHelper.getInt(globalEnv, VAR_TYPE, "playerHealth", 0);
                boolean hasKey = EnvironmentHelper.getBoolean(globalEnv, VAR_TYPE, "hasKey", false);

                System.out.println("\n📊 Game Info:");
                System.out.println("  Health: " + health);
                System.out.println("  Has Key: " + hasKey);
                System.out.println();
            }
        });

        tm.getAllTriggers().add(trigger);

        return obj;
    }

    private static void setPosition(GameObject obj, double x, double y) {
        Vector<Double> pos = new Vector<>();
        pos.add(x);
        pos.add(y);
        Vector<Double> scale = new Vector<>();
        scale.add(1.0);
        scale.add(1.0);
        obj.setTransform(new Transform(pos, 0f, scale));
    }
}