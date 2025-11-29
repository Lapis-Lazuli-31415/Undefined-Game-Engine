package app;

import data_access.saving.JsonProjectDataAccess;
import entity.GameObject;
import entity.Project;
import entity.Scene;
import entity.scripting.Trigger;
import entity.scripting.action.Action;
import entity.scripting.condition.Condition;

import java.io.IOException;

public class LoadDemo {
    public static void main(String[] args) {
        System.out.println("--- Starting Load Demo ---");

        JsonProjectDataAccess dataAccess = new JsonProjectDataAccess();

        try {
            // Load the project from your file
            // Make sure "test.json" exists in your project root folder
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

                    // Verify Transform
                    if (obj.getTransform() != null) {
                        System.out.printf("    Transform: X=%.1f, Y=%.1f, Rot=%.1f%n",
                                obj.getTransform().getX(),
                                obj.getTransform().getY(),
                                obj.getTransform().getRotation());
                    }

                    // Verify Triggers
                    if (obj.getTriggerManager() != null) {
                        System.out.println("    Triggers: " + obj.getTriggerManager().getTriggers().size());
                        for (Trigger t : obj.getTriggerManager().getTriggers()) {
                            System.out.println("      - Event: " + t.getEvent().getEventLabel());
                            System.out.println("        Conditions: " + t.getConditions().size());
                            System.out.println("        Actions: " + t.getActions().size());
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("❌ Failed to load project: " + e.getMessage());
            e.printStackTrace();
        }
    }
}