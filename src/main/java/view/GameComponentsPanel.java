package view;

import interface_adapter.add_scene.AddSceneController;
import interface_adapter.add_scene.AddScenePresenter;
import interface_adapter.add_scene.AddScenePresenter.SceneCreationListener;
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
 * It receives use-cases via constructor injection and updates the UI in response
 * to presenter callbacks.
 */
public class GameComponentsPanel extends JPanel
        implements ListScenesOutputBoundary,
        SceneSelectionListener,
        GameObjectSelectionListener,
        SceneCreationListener {

    private final JTree tree;
    private final DefaultTreeModel treeModel;
    private final DefaultMutableTreeNode root;

    private ListScenesInputBoundary listScenesUseCase;
    private SelectSceneInputBoundary selectSceneUseCase;
    private SelectGameObjectInputBoundary selectGameObjectUseCase;
    private final AddScenePresenter addScenePresenter;

    private final ScenePanel scenePanel;

    public GameComponentsPanel(
            ListScenesInputBoundary listScenesUseCase,
            SelectSceneInputBoundary selectSceneUseCase,
            SelectGameObjectInputBoundary selectGameObjectUseCase,
            AddScenePresenter addScenePresenter,
            AddSceneController addSceneController,
            ScenePanel scenePanel
    ) {

        this.listScenesUseCase = listScenesUseCase;
        this.selectSceneUseCase = selectSceneUseCase;
        this.selectGameObjectUseCase = selectGameObjectUseCase;
        this.addScenePresenter = addScenePresenter;
        this.scenePanel = scenePanel;

        // Register this panel as listener for scene creation events
        addScenePresenter.setListener(this);

        setLayout(new BorderLayout());
        setOpaque(false);

        root = new DefaultMutableTreeNode("Scenes");
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);

        JScrollPane scroll = new JScrollPane(tree);
        scroll.setPreferredSize(new Dimension(0, 200));

        add(scroll, BorderLayout.CENTER);

        // Selection behavior
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node == null) return;

            Object uo = node.getUserObject();

            if (uo instanceof SceneNode sn) {
                selectSceneUseCase.selectScene(sn.name);
            } else if (uo instanceof GameObjectNode gon) {
                GameObject placeholder = new GameObject(
                        gon.idOrName,
                        gon.name,
                        true,
                        null,
                        null
                );
                selectGameObjectUseCase.selectGameObject(gon.parentSceneName, placeholder);
            }
        });

        // Initial load
        SwingUtilities.invokeLater(() -> {
            if (listScenesUseCase != null) listScenesUseCase.listScenes();
        });
    }

    public void setInteractors(
            ListScenesInputBoundary listScenesUseCase,
            SelectSceneInputBoundary selectSceneUseCase,
            SelectGameObjectInputBoundary selectGameObjectUseCase
    ) {
        // Assign the use cases
        // (The fields are final in your current code — so we must REMOVE "final" first.)
        this.listScenesUseCase = listScenesUseCase;
        this.selectSceneUseCase = selectSceneUseCase;
        this.selectGameObjectUseCase = selectGameObjectUseCase;

        // Re-list scenes now that we have a real interactor
        if (this.listScenesUseCase != null) {
            SwingUtilities.invokeLater(() -> this.listScenesUseCase.listScenes());
        }
    }

    public void refreshScenes() {
        if (listScenesUseCase != null) {
            SwingUtilities.invokeLater(() -> {
                try {
                    listScenesUseCase.listScenes();
                } catch (Exception ex) {
                    System.err.println("[GameComponentsPanel] refreshScenes failed: " + ex.getMessage());
                }
            });
        }
    }


    // ========= LIST SCENES OUTPUT ==========
    @Override
    public void presentScenes(Map<String, List<String>> sceneData) {
        SwingUtilities.invokeLater(() -> {
            root.removeAllChildren();

            for (String sceneName : sceneData.keySet()) {
                DefaultMutableTreeNode sceneNode =
                        new DefaultMutableTreeNode(new SceneNode(sceneName));

                List<String> gos = sceneData.get(sceneName);
                if (gos != null) {
                    for (String goName : gos) {
                        sceneNode.add(new DefaultMutableTreeNode(
                                new GameObjectNode(goName, goName, sceneName)
                        ));
                    }
                }
                root.add(sceneNode);
            }

            treeModel.reload();
            expandAll(tree, true);

            // Auto-select the first scene when loading
            if (!sceneData.isEmpty() && selectSceneUseCase != null) {
                String firstSceneName = sceneData.keySet().iterator().next();
                selectSceneUseCase.selectScene(firstSceneName);
            }

        });
    }

    // ========= SCENE SELECTION LISTENER ==========
    @Override
    public void onSceneChange(Scene scene) {
        scenePanel.setScene(scene);
        System.out.println("Switched to scene: " + scene.getName());
        System.out.println("Scene has GO count: " + scene.getGameObjects().size());


        DefaultMutableTreeNode sceneNode = findSceneNode(scene.getName());
        if (sceneNode != null) {

            // Clear old children
            sceneNode.removeAllChildren();

            // Add new gameobject nodes
            if (scene.getGameObjects() != null) {
                for (GameObject go : scene.getGameObjects()) {
                    sceneNode.add(new DefaultMutableTreeNode(go.getName()));
                }
            }

            ((DefaultTreeModel) tree.getModel()).reload(sceneNode);

            TreePath path = new TreePath(sceneNode.getPath());
            tree.setSelectionPath(path);
        }
    }


    // ========= GAMEOBJECT SELECTION LISTENER ==========
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
                        TreePath path = new TreePath(child.getPath());
                        tree.setSelectionPath(path);
                        tree.scrollPathToVisible(path);
                        return;
                    }
                }
            }
        });
    }

    // ========= SCENE CREATION LISTENER ==========
    @Override
    public void onSceneCreated(Scene scene) {
        if (scenePanel != null) {
            scenePanel.setScene(scene);
             }
        if (selectSceneUseCase != null) selectSceneUseCase.selectScene(scene.getName());
        if (listScenesUseCase != null) listScenesUseCase.listScenes();

        SwingUtilities.invokeLater(() -> {
            DefaultMutableTreeNode node = findSceneNode(scene.getName());
            if (node != null) {
                TreePath p = new TreePath(node.getPath());
                tree.setSelectionPath(p);
                tree.scrollPathToVisible(p);
            }
        });
    }

    // ========= HELPERS ==========

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
        final String idOrName;
        final String name;
        final String parentSceneName;
        GameObjectNode(String idOrName, String name, String parentSceneName) {
            this.idOrName = idOrName;
            this.name = name;
            this.parentSceneName = parentSceneName;
        }
        public String toString() { return name; }
    }

    private void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        expandAll(new TreePath(root), expand);
    }

    private void expandAll(TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                expandAll(parent.pathByAddingChild(n), expand);
            }
        }
        if (expand) tree.expandPath(parent);
        else tree.collapsePath(parent);
    }

    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

}
