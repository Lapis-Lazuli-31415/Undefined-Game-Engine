//package app;
//
//import data_access.saving.JsonProjectDataAccess;
//import entity.GameObject;
//import entity.Project;
//import entity.Scene;
//import entity.scripting.Trigger;
//import entity.scripting.action.Action;
//import entity.scripting.condition.Condition;
//
//import java.io.IOException;
//import java.lang.reflect.Field;
//
//public class LoadDemo {
//    public static void main(String[] args) {
//        System.out.println("--- Starting Load Demo ---");
//
//        JsonProjectDataAccess dataAccess = new JsonProjectDataAccess();
//
//        try {
//            // Load the project from your file
//            // Make sure "test.json" exists in your project root folder
//            Project loadedProject = dataAccess.load("test.json");
//
//            System.out.println("✅ Project Loaded Successfully!");
//            System.out.println("==============================");
//
//            // 1. Verify Project Details
//            System.out.println("Project Name: " + loadedProject.getName());
//            System.out.println("Project ID:   " + loadedProject.getId());
//
//            // 2. Verify Assets
//            System.out.println("\n--- Assets ---");
//            loadedProject.getAssets().getAll().forEach(asset ->
//                    System.out.println(" - " + asset.getName() + " (" + asset.getClass().getSimpleName() + ")")
//            );
//
//            // 3. Verify Scenes and GameObjects
//            System.out.println("\n--- Scenes & Objects ---");
//            for (Scene scene : loadedProject.getScenes()) {
//                System.out.println("Scene: " + scene.getName());
//
//                for (GameObject obj : scene.getGameObjects()) {
//                    System.out.println("  Object: " + obj.getName());
//                    System.out.println("    Active: " + obj.isActive());
//
//                    // Verify Transform
//                    if (obj.getTransform() != null) {
//                        System.out.printf("    Transform: X=%.1f, Y=%.1f, Rot=%.1f%n",
//                                obj.getTransform().getX(),
//                                obj.getTransform().getY(),
//                                obj.getTransform().getRotation());
//                    }
//
//                    // Verify Triggers
//                    if (obj.getTriggerManager() != null) {
//                        System.out.println("    Triggers: " + obj.getTriggerManager().getTriggers().size());
//                        for (Trigger t : obj.getTriggerManager().getTriggers()) {
//                            System.out.println("      --------------------------");
//                            System.out.println("      Event: " + t.getEvent().getEventLabel());
//
//                            // --- Detailed Conditions ---
//                            System.out.println("      Conditions (" + t.getConditions().size() + "):");
//                            for (Condition c : t.getConditions()) {
//                                printObjectDetails(c, "        ");
//                            }
//
//                            // --- Detailed Actions ---
//                            System.out.println("      Actions (" + t.getActions().size() + "):");
//                            for (Action a : t.getActions()) {
//                                printObjectDetails(a, "        ");
//                            }
//                            System.out.println("      --------------------------");
//                        }
//                    }
//                }
//            }
//
//        } catch (IOException e) {
//            System.err.println("❌ Failed to load project: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Helper method to print the class name and all fields (variables/values)
//     * of an object using Reflection.
//     */
//    private static void printObjectDetails(Object obj, String indent) {
//        if (obj == null) return;
//
//        Class<?> clazz = obj.getClass();
//        System.out.println(indent + "Type: [" + clazz.getSimpleName() + "]");
//
//        // Iterate over all declared fields in the class
//        Field[] fields = clazz.getDeclaredFields();
//        if (fields.length == 0) {
//            System.out.println(indent + "  (No variables)");
//        } else {
//            for (Field field : fields) {
//                field.setAccessible(true); // Allow access to private fields
//                try {
//                    Object value = field.get(obj);
//                    System.out.println(indent + "  - " + field.getName() + ": " + value);
//                } catch (IllegalAccessException e) {
//                    System.out.println(indent + "  - " + field.getName() + ": [Access Denied]");
//                }
//            }
//        }
//    }
//}


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
            Project loadedProject = dataAccess.load("test.json");

            System.out.println("✅ Project Loaded Successfully!");
            System.out.println("==============================");

            // 1. Verify Project Details
            System.out.println("Project Name: " + loadedProject.getName());
            System.out.println("Project ID:   " + loadedProject.getId());

            // 2. Verify Assets
            System.out.println("\n--- Assets ---");
            loadedProject.getAssets().getAll().forEach(asset ->
                    System.out.println(" - " + asset.getName() + " (" + asset.getClass().getSimpleName() + ")")
            );

            // 3. Verify Scenes and GameObjects
            System.out.println("\n--- Scenes & Objects ---");
            for (Scene scene : loadedProject.getScenes()) {
                System.out.println("Scene: " + scene.getName());

                for (GameObject obj : scene.getGameObjects()) {
                    System.out.println("  Object: " + obj.getName());
                    System.out.println("    Active: " + obj.isActive());

                    if (obj.getTransform() != null) {
                        System.out.printf("    Transform: X=%.1f, Y=%.1f, Rot=%.1f%n",
                                obj.getTransform().getX(),
                                obj.getTransform().getY(),
                                obj.getTransform().getRotation());
                    }

                    if (obj.getTriggerManager() != null) {
                        System.out.println("    Triggers: " + obj.getTriggerManager().getTriggers().size());
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
                }
            }

        } catch (IOException e) {
            System.err.println("❌ Failed to load project: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Recursively prints the fields of an object, including fields from parent classes.
     * Format: [Type] VariableName: Value
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
                // Skip static fields if you want to reduce noise (optional)
                if (Modifier.isStatic(field.getModifiers())) continue;

                field.setAccessible(true);
                try {
                    Object value = field.get(obj);
                    String fieldType = field.getType().getSimpleName();
                    String fieldName = field.getName();

                    if (isSimpleType(value)) {
                        // Format: - [String] myName: "Bob"
                        System.out.println(indent + "  - [" + fieldType + "] " + fieldName + ": " + value);
                    } else {
                        // Format: - [NumericValue] speed:
                        System.out.println(indent + "  - [" + fieldType + "] " + fieldName + ":");
                        printObjectDetails(value, indent + "      ");
                    }
                } catch (IllegalAccessException e) {
                    System.out.println(indent + "  - " + field.getName() + ": [Access Denied]");
                }
            }
            // Move up to the parent class (e.g. from NumericValue -> Value)
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