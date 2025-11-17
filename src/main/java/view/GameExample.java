package view;

import entity.GameObject;
import entity.Scene;
import entity.Transform;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.event.OnKeyPressEvent;
import entity.scripting.event.OnClickEvent;
import entity.scripting.action.Action;
import entity.scripting.environment.Environment;
import entity.scripting.environment.EnvironmentHelper;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Realistic game example - Using correct Environment API
 */
public class GameExample {

    private static final String VAR_TYPE = "game";

    public static void main(String[] args) {
        Scene scene = createGameScene();
        Environment globalEnv = new Environment();

        // Initialize game state using correct API
        EnvironmentHelper.setInt(globalEnv, VAR_TYPE, "playerHealth", 100);
        EnvironmentHelper.setInt(globalEnv, VAR_TYPE, "ammo", 30);
        EnvironmentHelper.setInt(globalEnv, VAR_TYPE, "coins", 0);
        EnvironmentHelper.setInt(globalEnv, VAR_TYPE, "currentWeapon", 1);

        PreviewWindow window = new PreviewWindow(scene, globalEnv);
        window.display();

        System.out.println("\n=== GAME CONTROLS ===");
        System.out.println("\nMOVEMENT:");
        System.out.println("  W/A/S/D    - Move");
        System.out.println("  Space      - Jump");
        System.out.println("  Shift      - Sprint");
        System.out.println("  Control    - Crouch");

        System.out.println("\nCOMBAT:");
        System.out.println("  1/2/3      - Switch weapon");
        System.out.println("  R          - Reload");
        System.out.println("  Click      - Shoot");

        System.out.println("\nINTERACTION:");
        System.out.println("  E          - Use/Interact");
        System.out.println("  Q          - Drop item");
        System.out.println("  F          - Flashlight");

        System.out.println("\nUI/MENU:");
        System.out.println("  Tab        - Inventory");
        System.out.println("  Escape     - Menu");
        System.out.println("  M          - Map");
        System.out.println("  I          - Show Info");

        System.out.println("\nQUICK ACTIONS:");
        System.out.println("  F1         - Help");
        System.out.println("  F5         - Quick Save");
        System.out.println("  F9         - Quick Load");
        System.out.println("\n");
    }

    private static Scene createGameScene() {
        ArrayList<GameObject> objects = new ArrayList<>();

        // === MOVEMENT ===
        objects.add(createAction("MoveForward", "W",
                (g, l) -> System.out.println("🏃 Moving forward")));

        objects.add(createAction("MoveLeft", "A",
                (g, l) -> System.out.println("🏃 Moving left")));

        objects.add(createAction("MoveBack", "S",
                (g, l) -> System.out.println("🏃 Moving back")));

        objects.add(createAction("MoveRight", "D",
                (g, l) -> System.out.println("🏃 Moving right")));

        objects.add(createAction("Jump", "Space",
                (g, l) -> System.out.println("🦘 Jumping!")));

        objects.add(createAction("Sprint", "Shift",
                (g, l) -> System.out.println("💨 Sprinting!")));

        objects.add(createAction("Crouch", "Control",
                (g, l) -> System.out.println("🐢 Crouching")));

        // === COMBAT ===
        objects.add(createAction("Weapon1", "1",
                (g, l) -> {
                    EnvironmentHelper.setInt(g, VAR_TYPE, "currentWeapon", 1);
                    System.out.println("🔫 Switched to Pistol");
                }));

        objects.add(createAction("Weapon2", "2",
                (g, l) -> {
                    EnvironmentHelper.setInt(g, VAR_TYPE, "currentWeapon", 2);
                    System.out.println("🔫 Switched to Rifle");
                }));

        objects.add(createAction("Weapon3", "3",
                (g, l) -> {
                    EnvironmentHelper.setInt(g, VAR_TYPE, "currentWeapon", 3);
                    System.out.println("🔫 Switched to Shotgun");
                }));

        objects.add(createAction("Reload", "R",
                (g, l) -> {
                    EnvironmentHelper.setInt(g, VAR_TYPE, "ammo", 30);
                    System.out.println("🔄 Reloading... Ammo: 30");
                }));

        // === INTERACTION ===
        objects.add(createAction("Interact", "E",
                (g, l) -> System.out.println("🤝 Interacting with object")));

        objects.add(createAction("DropItem", "Q",
                (g, l) -> System.out.println("📦 Item dropped")));

        objects.add(createAction("Flashlight", "F",
                (g, l) -> System.out.println("🔦 Flashlight toggled")));

        // === UI/MENU ===
        objects.add(createAction("Inventory", "Tab",
                (g, l) -> System.out.println("🎒 Opening inventory")));

        objects.add(createAction("Menu", "Escape",
                (g, l) -> System.out.println("📋 Opening menu")));

        objects.add(createAction("Map", "M",
                (g, l) -> System.out.println("🗺️ Opening map")));

        objects.add(createAction("ShowInfo", "I",
                (g, l) -> {
                    int health = EnvironmentHelper.getInt(g, VAR_TYPE, "playerHealth", 0);
                    int ammo = EnvironmentHelper.getInt(g, VAR_TYPE, "ammo", 0);
                    int coins = EnvironmentHelper.getInt(g, VAR_TYPE, "coins", 0);
                    int weapon = EnvironmentHelper.getInt(g, VAR_TYPE, "currentWeapon", 0);

                    System.out.println("\n📊 Player Stats:");
                    System.out.println("  Health: " + health);
                    System.out.println("  Ammo: " + ammo);
                    System.out.println("  Coins: " + coins);
                    System.out.println("  Weapon: " + weapon);
                    System.out.println();
                }));

        // === QUICK ACTIONS ===
        objects.add(createAction("Help", "F1",
                (g, l) -> System.out.println("❓ Help menu opened")));

        objects.add(createAction("QuickSave", "F5",
                (g, l) -> System.out.println("💾 Game saved!")));

        objects.add(createAction("QuickLoad", "F9",
                (g, l) -> System.out.println("📂 Game loaded!")));

        // === CLICK ACTION ===
        objects.add(createClickAction("Shoot", 300, 50,
                (g, l) -> {
                    int ammo = EnvironmentHelper.getInt(g, VAR_TYPE, "ammo", 0);
                    if (ammo > 0) {
                        EnvironmentHelper.setInt(g, VAR_TYPE, "ammo", ammo - 1);
                        System.out.println("💥 Shooting! Ammo left: " + (ammo - 1));
                    } else {
                        System.out.println("🔴 Out of ammo! Press R to reload");
                    }
                }));

        return new Scene("game-example", "Game Example", objects, null);
    }

    private static GameObject createAction(String name, String key, ActionExecutor executor) {
        GameObject obj = new GameObject(
                name, name, true,
                new ArrayList<>(),
                new Environment()
        );

        setPosition(obj, 0, 0);

        TriggerManager tm = obj.getTriggerManager();
        Trigger trigger = new Trigger(new OnKeyPressEvent(key));

        trigger.addAction(new Action() {
            @Override
            public void execute(Environment globalEnv, Environment localEnv) {
                executor.execute(globalEnv, localEnv);
            }
        });

        tm.getAllTriggers().add(trigger);

        return obj;
    }

    private static GameObject createClickAction(String name, double x, double y, ActionExecutor executor) {
        GameObject obj = new GameObject(
                name, name, true,
                new ArrayList<>(),
                new Environment()
        );

        setPosition(obj, x, y);

        TriggerManager tm = obj.getTriggerManager();
        Trigger trigger = new Trigger(new OnClickEvent());

        trigger.addAction(new Action() {
            @Override
            public void execute(Environment globalEnv, Environment localEnv) {
                executor.execute(globalEnv, localEnv);
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

    @FunctionalInterface
    interface ActionExecutor {
        void execute(Environment globalEnv, Environment localEnv);
    }
}