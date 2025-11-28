package interface_adapter.list_scenes;

import use_case.component_management.list_scenes.ListScenesOutputBoundary;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.List;
import java.util.Map;

public class ListScenesPresenter implements ListScenesOutputBoundary {
    private final DefaultTreeModel treeModel;

    public ListScenesPresenter(DefaultTreeModel treeModel) {
        this.treeModel = treeModel;
    }

    @Override
    public void presentScenes(Map<String, List<String>> sceneData) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Scenes");

        for (var entry : sceneData.entrySet()) {
            String sceneName = entry.getKey();
            List<String> gameObjects = entry.getValue();
            DefaultMutableTreeNode sceneNode = new DefaultMutableTreeNode(sceneName);
            for (String gameObject : gameObjects) {
                sceneNode.add(new DefaultMutableTreeNode(gameObject));
            }
            root.add(sceneNode);
        }

        treeModel.setRoot(root);
        treeModel.reload();
    }
}
