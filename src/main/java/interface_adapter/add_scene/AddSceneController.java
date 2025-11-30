package interface_adapter.add_scene;

import use_case.component_management.add_scene.AddSceneInputBoundary;

public class AddSceneController {

    private final AddSceneInputBoundary addSceneUseCase;

    public AddSceneController(AddSceneInputBoundary addSceneUseCase) {
        this.addSceneUseCase = addSceneUseCase;
    }

    public void addScene(String name) {
        addSceneUseCase.addScene(name);
    }
}
