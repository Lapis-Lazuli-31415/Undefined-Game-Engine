package interface_adapter.create_scene;

import use_case.component_management.create_scene.CreateSceneInputBoundary;
import use_case.component_management.create_scene.CreateSceneInputData;

public class CreateSceneController {
    private final CreateSceneInputBoundary createSceneInteractor;

    public CreateSceneController(CreateSceneInputBoundary createSceneInteractor) {
        this.createSceneInteractor = createSceneInteractor;
    }

    public void createScene(String name) {
        createSceneInteractor.execute(new CreateSceneInputData(name));
    }
}
