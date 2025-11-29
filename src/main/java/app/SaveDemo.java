package app;

import data_access.saving.JsonProjectDataAccess;
import entity.*;
import entity.Image;
import entity.scripting.Trigger;
import entity.scripting.environment.Environment;
import entity.scripting.event.EmptyEvent;
import interface_adapter.saving.SaveProjectController;
import interface_adapter.saving.SaveProjectPresenter;
import interface_adapter.saving.SaveProjectViewModel;
import use_case.saving.SaveProjectInteractor;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;


import java.awt.*;
/**
 * A standalone runner to test the Saving Feature.
 * Right-click this file and select "Run 'SaveDemo.main()'"
 */
public class SaveDemo {

    public static void main(String[] args) {
        System.out.println("--- Starting Save Demo ---");

        // CREATE DATA (The Sample Project)
        Project projectToSave = createSampleProject();
        System.out.println("Created Project: " + projectToSave.getName());

        // SETUP ARCHITECTURE
        // data access
        JsonProjectDataAccess dataAccess = new JsonProjectDataAccess();

        // presenter and viewmodel
        SaveProjectViewModel viewModel = new SaveProjectViewModel();
        SaveProjectPresenter presenter = new SaveProjectPresenter(viewModel);

        // use case
        SaveProjectInteractor interactor = new SaveProjectInteractor(
                dataAccess,
                presenter,
                projectToSave
        );

        // controller
        SaveProjectController controller = new SaveProjectController(interactor);

        // execute
        System.out.println("Saving to 'database.json'...");
        controller.execute(null); // null means "use existing project name"

        // RESULTS
        // TODO: have view respond to this, but for now I made it just check the state.
        String resultMessage = viewModel.getState().getMessage();
        String errorMessage = viewModel.getState().getError();

        if (errorMessage != null) {
            System.err.println("FAILED: " + errorMessage);
        } else {
            System.out.println("SUCCESS: " + resultMessage);
        }
    }

    // HELPER TO BUILD PROJECT
    private static Project createSampleProject() {
        // global environment
        Environment globalEnv = new Environment();
        GameController gameController = new GameController(globalEnv);

        // transform for the Bear
        Vector<Double> pos = new Vector<>(); pos.add(100.0); pos.add(200.0);
        Vector<Double> scale = new Vector<>(); scale.add(1.0); scale.add(1.0);
        Transform transform = new Transform(pos, 45.0f, scale);

        // asset library list
        AssetLib assetLib = new AssetLib();

        // properties (Sprite)
        ArrayList<Property> properties = new ArrayList<>();



        try {
            // pass the path to the image
            java.nio.file.Path bearPath = java.nio.file.Path.of("src/main/resources/bear.png");
            Image image = new Image(bearPath);

            // add the image to the assetLib
            assetLib.add(image);

            properties.add(new SpriteRenderer(image, true));

        } catch (java.io.IOException e) {
            System.err.println("Could not load image for demo: " + e.getMessage());
            // create a dummy property or exit if image fails
        }

        // gameObject
        GameObject bear = new GameObject(
                "obj-bear", "Bear", true, properties, new Environment()
        );
        bear.setTransform(transform);

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