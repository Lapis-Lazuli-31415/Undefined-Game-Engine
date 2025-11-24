package use_case.create_scene;

public class CreateSceneInputData {
    private final String name;

    public CreateSceneInputData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
