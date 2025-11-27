package use_case.component_management.list_scenes;

import java.util.List;
import java.util.Map;

public interface ListScenesOutputBoundary {
    void presentScenes(Map<String, List<String>> sceneData);
}
