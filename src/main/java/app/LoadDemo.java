package app;

import data_access.saving.JsonProjectDataAccess;
import entity.GameObject;
import entity.Project;
import entity.Scene;
import entity.scripting.Trigger;
import entity.scripting.action.Action;
import entity.scripting.condition.Condition;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class LoadDemo {
    public static void main(String[] args) {
        System.out.println("--- Starting Load Demo ---");

        JsonProjectDataAccess dataAccess = new JsonProjectDataAccess();

        try {
            // Load the project from the file saved by SaveDemo
            Project loadedProject = dataAccess.load("database.json");

            System.out.println("✅ Project Loaded Successfully!");
            System.out.println("==============================");

            // 1. Verify Project Details
            System.out.println("Project Name: " + loadedProject.getName());
            System.out.println("Project ID:   " + loadedProject.getId());

            // 2. Verify Assets
            System.out.println("\n--- Assets ---");
            if (loadedProject.getAssets() != null && loadedProject.getAssets().getAll() != null) {
                loadedProject.getAssets().getAll().forEach(asset ->
                        System.out.println(" - " + asset.getName() + " [" + asset.getClass().getSimpleName() + "]")
                );
            }

            // 3. Verify Scenes and GameObjects
            System.out.println("\n--- Scenes & Objects ---");
            if (loadedProject.getScenes() != null) {
                for (Scene scene : loadedProject.getScenes()) {
                    System.out.println("Scene: " + scene.getName());

                    for (GameObject obj : scene.getGameObjects()) {
                        System.out.println("  Object: " + obj.getName());
                        System.out.println("    Active: " + obj.isActive());

                        // Verify SpriteRenderer
                        if (obj.getSpriteRenderer() != null) {
                            System.out.println("    [SpriteRenderer]");
                            if (obj.getSpriteRenderer().getSprite() != null) {
                                System.out.println("      - Image Source: " + obj.getSpriteRenderer().getSprite().getName());
                            } else {
                                System.out.println("      - Image Source: None");
                            }
                            System.out.println("      - Visible: " + obj.getSpriteRenderer().getVisible());
                            System.out.println("      - Opacity: " + obj.getSpriteRenderer().getOpacity());
                        } else {
                            System.out.println("    [SpriteRenderer]: None");
                        }

                        // Verify Transform
                        if (obj.getTransform() != null) {
                            System.out.printf("    [Transform] X=%.1f, Y=%.1f, Rot=%.1f, ScaleX=%.1f%n",
                                    obj.getTransform().getX(),
                                    obj.getTransform().getY(),
                                    obj.getTransform().getRotation(),
                                    obj.getTransform().getScaleX());
                        }

                        // Verify Triggers
                        if (obj.getTriggerManager() != null) {
                            System.out.println("    [Triggers]: " + obj.getTriggerManager().getTriggers().size());
                            for (Trigger t : obj.getTriggerManager().getTriggers()) {
                                System.out.println("      --------------------------");
                                System.out.println("      Event: " + t.getEvent().getEventLabel());

                                System.out.println("      Conditions (" + t.getConditions().size() + "):");
                                for (Condition c : t.getConditions()) {
                                    printObjectDetails(c, "        ");
                                }

                                System.out.println("      Actions (" + t.getActions().size() + "):");
                                for (Action a : t.getActions()) {
                                    printObjectDetails(a, "        ");
                                }
                                System.out.println("      --------------------------");
                            }
                        }
                        System.out.println(); // Space between objects
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Failed to load project: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Recursively prints the fields of an object, including fields from parent classes.
     */
    private static void printObjectDetails(Object obj, String indent) {
        if (obj == null) return;

        Class<?> clazz = obj.getClass();

        // Direct print for simple types
        if (isSimpleType(obj)) {
            System.out.println(indent + obj);
            return;
        }

        System.out.println(indent + "Object: [" + clazz.getSimpleName() + "]");

        // Loop up the class hierarchy to find private fields in parent classes too
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();

            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) continue;

                field.setAccessible(true);
                try {
                    Object value = field.get(obj);
                    String fieldType = field.getType().getSimpleName();
                    String fieldName = field.getName();

                    if (isSimpleType(value)) {
                        System.out.println(indent + "  - " + fieldName + ": " + value);
                    } else {
                        System.out.println(indent + "  - " + fieldName + ":");
                        printObjectDetails(value, indent + "      ");
                    }
                } catch (IllegalAccessException e) {
                    System.out.println(indent + "  - " + field.getName() + ": [Access Denied]");
                }
            }
            currentClass = currentClass.getSuperclass();
        }
    }

    private static boolean isSimpleType(Object obj) {
        if (obj == null) return true;
        return obj instanceof String ||
                obj instanceof Number ||
                obj instanceof Boolean ||
                obj.getClass().isEnum();
    }
}