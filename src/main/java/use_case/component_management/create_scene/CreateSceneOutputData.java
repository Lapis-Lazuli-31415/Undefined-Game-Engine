package use_case.component_management.create_scene;

import java.util.UUID;

public class CreateSceneOutputData {
    private final UUID id;
    private final String name;

    public CreateSceneOutputData(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
