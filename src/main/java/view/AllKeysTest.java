package view;

import entity.GameObject;
import entity.Scene;
import entity.Transform;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.event.OnKeyPressEvent;
import entity.scripting.action.Action;
import entity.scripting.environment.Environment;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Comprehensive test showing ALL types of keys that work
 */
public class AllKeysTest {

    public static void main(String[] args) {
        Scene scene = createTestScene();
        Environment globalEnv = new Environment();

        PreviewWindow window = new PreviewWindow(scene, globalEnv);
        window.display();

        System.out.println("\n=== ALL KEYS TEST ===");
        System.out.println("\n1. LETTER KEYS (A-Z):");
        System.out.println("   Try: Q, E, R, F, G, etc.");

        System.out.println("\n2. NUMBER KEYS (0-9):");
        System.out.println("   Try: 1, 2, 3, 4, 5, etc.");

        System.out.println("\n3. ARROW KEYS:");
        System.out.println("   Try: Up, Down, Left, Right");

        System.out.println("\n4. FUNCTION KEYS:");
        System.out.println("   Try: F1, F2, F3, F12");

        System.out.println("\n5. SPECIAL KEYS:");
        System.out.println("   Try: Space, Enter, Escape, Tab");

        System.out.println("\n6. MODIFIER KEYS:");
        System.out.println("   Try: Shift, Control, Alt");

        System.out.println("\nClick Play and try pressing different keys!\n");
    }

    private static Scene createTestScene() {
        ArrayList<GameObject> objects = new ArrayList<>();

        int y = 50;
        int spacing = 60;

        // === SECTION 1: Letter Keys ===
        System.out.println("Creating Letter Key Objects...");
        objects.add(createKeyObject("Move_Forward", "W", 50, y));
        y += spacing;
        objects.add(createKeyObject("Move_Left", "A", 50, y));
        y += spacing;
        objects.add(createKeyObject("Move_Back", "S", 50, y));
        y += spacing;
        objects.add(createKeyObject("Move_Right", "D", 50, y));
        y += spacing;
        objects.add(createKeyObject("Interact", "E", 50, y));
        y += spacing;
        objects.add(createKeyObject("Reload", "R", 50, y));
        y += spacing;
        objects.add(createKeyObject("Use_Item", "Q", 50, y));
        y += spacing;
        objects.add(createKeyObject("Flashlight", "F", 50, y));

        // === SECTION 2: Number Keys ===
        y = 50;
        System.out.println("Creating Number Key Objects...");
        for (int i = 1; i <= 5; i++) {
            objects.add(createKeyObject("Weapon_" + i, String.valueOf(i), 250, y));
            y += spacing;
        }

        // === SECTION 3: Arrow Keys ===
        y = 50;
        System.out.println("Creating Arrow Key Objects...");
        objects.add(createKeyObject("Look_Up", "Up", 450, y));
        y += spacing;
        objects.add(createKeyObject("Look_Down", "Down", 450, y));
        y += spacing;
        objects.add(createKeyObject("Look_Left", "Left", 450, y));
        y += spacing;
        objects.add(createKeyObject("Look_Right", "Right", 450, y));

        // === SECTION 4: Special Keys ===
        y += spacing;
        System.out.println("Creating Special Key Objects...");
        objects.add(createKeyObject("Jump", "Space", 450, y));
        y += spacing;
        objects.add(createKeyObject("Confirm", "Enter", 450, y));
        y += spacing;
        objects.add(createKeyObject("Menu", "Escape", 450, y));
        y += spacing;
        objects.add(createKeyObject("Switch", "Tab", 450, y));

        // === SECTION 5: Function Keys ===
        y = 50;
        System.out.println("Creating Function Key Objects...");
        objects.add(createKeyObject("Help", "F1", 650, y));
        y += spacing;
        objects.add(createKeyObject("Quick_Save", "F5", 650, y));
        y += spacing;
        objects.add(createKeyObject("Quick_Load", "F9", 650, y));
        y += spacing;
        objects.add(createKeyObject("Screenshot", "F12", 650, y));

        // === SECTION 6: Modifier Keys ===
        y += spacing;
        System.out.println("Creating Modifier Key Objects...");
        objects.add(createKeyObject("Sprint", "Shift", 650, y));
        y += spacing;
        objects.add(createKeyObject("Crouch", "Control", 650, y));

        System.out.println("Total objects created: " + objects.size());

        return new Scene("all-keys-test", "All Keys Test", objects, null);
    }

    private static GameObject createKeyObject(String name, String key, double x, double y) {
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
                System.out.println("⌨️  [" + key + "] → " + name);
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