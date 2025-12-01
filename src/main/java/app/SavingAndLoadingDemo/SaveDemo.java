package app.SavingAndLoadingDemo;

import data_access.saving.JsonProjectDataAccess;
import entity.*;
import entity.Image;
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
import interface_adapter.saving.SaveProjectController;
import interface_adapter.saving.SaveProjectPresenter;
import interface_adapter.saving.SaveProjectViewModel;
import use_case.saving.SaveProjectInteractor;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

public class SaveDemo {

    public static void main(String[] args) {
        System.out.println("--- Starting Save Demo ---");

        // CREATE DATA (The Sample Project)
        Project projectToSave = createSampleProject();
        System.out.println("Created Project: " + projectToSave.getName());

        // SETUP ARCHITECTURE
        JsonProjectDataAccess dataAccess = new JsonProjectDataAccess();
        SaveProjectViewModel viewModel = new SaveProjectViewModel();
        SaveProjectPresenter presenter = new SaveProjectPresenter(viewModel);

        SaveProjectInteractor interactor = new SaveProjectInteractor(
                dataAccess,
                presenter,
                projectToSave
        );

        SaveProjectController controller = new SaveProjectController(interactor);

        // execute
        System.out.println("Saving to 'database.json'...");
        controller.execute(null); // null means "use existing project name"

        // RESULTS
        String resultMessage = viewModel.getState().getMessage();
        String errorMessage = viewModel.getState().getError();

        if (errorMessage != null) {
            System.err.println("FAILED: " + errorMessage);
        } else {
            System.out.println("SUCCESS: " + resultMessage);
            System.out.println("Check 'database.json' to see variables and triggers!");
        }
    }

    // HELPER TO BUILD PROJECT
    private static Project createSampleProject() {
        // 1. Setup Global Environment with Variables
        Environment globalEnv = new Environment();
        try {
            Assign.assign(globalEnv, new NumericVariable("wins", true), 0.0);
            Assign.assign(globalEnv, new NumericVariable("losses", true), 0.0);
            Assign.assign(globalEnv, new BooleanVariable("win", true), false);
        } catch (Exception e) {
            System.err.println("Error creating global variables: " + e.getMessage());
        }

        GameController gameController = new GameController(globalEnv);

        // 2. Setup Bear Object Environment with Variables
        Environment bearEnv = new Environment();
        try {
            Assign.assign(bearEnv, new NumericVariable("health", false), 100.0);
            Assign.assign(bearEnv, new BooleanVariable("alive", false), true);
        } catch (Exception e) {
            System.err.println("Error creating local variables: " + e.getMessage());
        }

        // transform for the Bear
        Vector<Double> pos = new Vector<>(); pos.add(100.0); pos.add(200.0);
        Vector<Double> scale = new Vector<>(); scale.add(1.0); scale.add(1.0);
        Transform transform = new Transform(pos, 45.0f, scale);

        // asset library list
        AssetLib assetLib = new AssetLib();
        SpriteRenderer bearRenderer = null;

        // Load Sprite from 'uploads'
        try {
            // Pointing to the file you have in your uploads folder
            java.nio.file.Path spritePath = java.nio.file.Path.of("uploads", null);

            // Create the Image entity
            // (The Image class constructor reads the file to get width/height)
            Image image = new Image(spritePath);

            // Add it to the AssetLib
            assetLib.add(image);

            // 3. Create the SpriteRenderer component
            bearRenderer = new SpriteRenderer(image, true);

            System.out.println("Loaded sprite from uploads: " + image.getName());

        } catch (java.io.IOException e) {
            System.err.println("Could not load image from uploads: " + e.getMessage());
            // Fallback so the demo doesn't crash if the file is missing
            bearRenderer = null;
        }

        // 3. Create TriggerManager
        TriggerManager triggerManager = new TriggerManager();

        // 4. Create GameObject
        GameObject bear = new GameObject(
                "obj-bear", "Bear", true, bearEnv, transform, bearRenderer, triggerManager
        );
        bear.setTransform(transform);



        // Create a sample Trigger: On Click -> If Health > 0 -> Wait 1.0s
        Trigger clickTrigger = new Trigger(new OnClickEvent(), true);

        // Condition: health > 0
        clickTrigger.addCondition(new NumericComparisonCondition(
                new NumericVariable("health", false),
                ">",
                new NumericValue(0)
        ));

        // Action: Wait 1 second
        clickTrigger.addAction(new WaitAction(new NumericValue(1.0)));

        triggerManager.addTrigger(clickTrigger);

        // scene
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(bear);
        Scene scene = new Scene(UUID.randomUUID(), "Forest Level", objects);

        // project
        ArrayList<Scene> scenes = new ArrayList<>();
        scenes.add(scene);

        return new Project("proj-001", "project_name", scenes, assetLib, gameController);
    }
}