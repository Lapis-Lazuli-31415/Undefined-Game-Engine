package view;

import use_case.component_management.list_scenes.ListScenesInputBoundary;
import use_case.component_management.list_scenes.ListScenesOutputBoundary;
import use_case.component_management.select_scene.SelectSceneInputBoundary;
import use_case.component_management.select_game_object.SelectGameObjectInputBoundary;
import interface_adapter.select_scene.SelectScenePresenter.SceneSelectionListener;
import interface_adapter.select_game_object.SelectGameObjectPresenter.GameObjectSelectionListener;

import entity.Scene;
import entity.GameObject;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Enumeration;

/**
 * GameComponentsPanel renders a hierarchy of Scenes -> GameObjects using a JTree.
 * It implements ListScenesOutputBoundary so it can be passed directly to ListScenesInteractor,
 * and implements the presenters' listener interfaces to be notified of selections.
 *
 * NOTE: When selecting a GameObject from the tree, it constructs a minimal placeholder
 * GameObject (by name) and calls the SelectGameObjectInputBoundary. If you need full
 * identity mapping, adapt this panel to keep a map from names to entities.
 */
public class GameComponentsPanel extends JPanel
        implements ListScenesOutputBoundary, SceneSelectionListener, GameObjectSelectionListener {

    private final JTree tree;
    private final DefaultTreeModel treeModel;
    private final DefaultMutableTreeNode root;

    private final ListScenesInputBoundary listScenesUseCase;
    private final SelectSceneInputBoundary selectSceneUseCase;
    private final SelectGameObjectInputBoundary selectGameObjectUseCase;

    public GameComponentsPanel(ListScenesInputBoundary listScenesUseCase,
                               SelectSceneInputBoundary selectSceneUseCase,
                               SelectGameObjectInputBoundary selectGameObjectUseCase) {

        this.listScenesUseCase = listScenesUseCase;
        this.selectSceneUseCase = selectSceneUseCase;
        this.selectGameObjectUseCase = selectGameObjectUseCase;

        setLayout(new BorderLayout());
        setOpaque(false);

        root = new DefaultMutableTreeNode("Scenes");
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);

        JScrollPane scroll = new JScrollPane(tree);
        scroll.setPreferredSize(new Dimension(0, 200)); // let layout control width

        add(scroll, BorderLayout.CENTER);

        // Selection behavior: dispatch to use-cases depending on node type
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node == null) return;

            Object uo = node.getUserObject();
            if (uo instanceof SceneNode) {
                String sceneName = ((SceneNode) uo).name;
                selectSceneUseCase.selectScene(sceneName);
            } else if (uo instanceof GameObjectNode) {
                GameObjectNode gon = (GameObjectNode) uo;
                // pass a minimal placeholder GameObject (by name). Interactor checks by name/contains.
                GameObject placeholder = new GameObject(gon.idOrName, gon.name, true, null, null);
                selectGameObjectUseCase.selectGameObject(gon.parentSceneName, placeholder);
            }
        });

        // initially request the list
        SwingUtilities.invokeLater(() -> {
            if (listScenesUseCase != null) listScenesUseCase.listScenes();
        });
    }

    // ListScenesOutputBoundary implementation
    @Override
    public void presentScenes(Map<String, List<String>> sceneData) {
        SwingUtilities.invokeLater(() -> {
            root.removeAllChildren();
            for (String sceneName : sceneData.keySet()) {
                DefaultMutableTreeNode sceneNode = new DefaultMutableTreeNode(new SceneNode(sceneName));
                List<String> gos = sceneData.get(sceneName);
                if (gos != null) {
                    for (String goName : gos) {
                        // Use name as idOrName for placeholder; adapt if you have stable IDs
                        DefaultMutableTreeNode goNode = new DefaultMutableTreeNode(new GameObjectNode(goName, goName, sceneName));
                        sceneNode.add(goNode);
                    }
                }
                root.add(sceneNode);
            }
            treeModel.reload();
            expandAll(tree, true);
        });
    }

    // Presenter listener when a full Scene entity is selected by the interactor -> presenter
    @Override
    public void onSceneChange(Scene scene) {
        // Select the scene node in the tree and expand it
        SwingUtilities.invokeLater(() -> {
            DefaultMutableTreeNode found = findSceneNode(scene.getName());
            if (found != null) {
                TreePath p = new TreePath(found.getPath());
                tree.setSelectionPath(p);
                tree.scrollPathToVisible(p);
            }
        });
    }

    // Presenter listener when a GameObject is selected by the interactor -> presenter
    @Override
    public void onGameObjectSelected(Scene scene, GameObject gameObject) {
        SwingUtilities.invokeLater(() -> {
            DefaultMutableTreeNode sceneNode = findSceneNode(scene.getName());
            if (sceneNode == null) return;
            Enumeration<?> en = sceneNode.children();
            while (en.hasMoreElements()) {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) en.nextElement();
                Object uo = child.getUserObject();
                if (uo instanceof GameObjectNode gon) {
                    if (gon.name.equals(gameObject.getName())) {
                        TreePath p = new TreePath(child.getPath());
                        tree.setSelectionPath(p);
                        tree.scrollPathToVisible(p);
                        return;
                    }
                }
            }
        });
    }

    private DefaultMutableTreeNode findSceneNode(String sceneName) {
        Enumeration<?> en = root.children();
        while (en.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();
            Object uo = node.getUserObject();
            if (uo instanceof SceneNode && ((SceneNode) uo).name.equals(sceneName)) {
                return node;
            }
        }
        return null;
    }

    private static class SceneNode {
        final String name;
        SceneNode(String name) { this.name = name; }
        public String toString() { return name; }
    }

    private static class GameObjectNode {
        final String idOrName; // placeholder for id if you have one
        final String name;
        final String parentSceneName;
        GameObjectNode(String idOrName, String name, String parentSceneName) {
            this.idOrName = idOrName; this.name = name; this.parentSceneName = parentSceneName;
        }
        public String toString() { return name; }
    }

    private void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        // Traverse tree from root
        expandAll(new TreePath(root), expand);
    }

    private void expandAll(TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(path, expand);
            }
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
}
