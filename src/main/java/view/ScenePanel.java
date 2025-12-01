package view;

import data_access.InMemorySceneRepository;
import entity.GameObject;
import entity.Scene; // Import Scene
import entity.SpriteRenderer;
import entity.Transform;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.action.Action;
import entity.scripting.condition.Condition;
import entity.scripting.environment.Environment;
import interface_adapter.EditorState;
import interface_adapter.transform.TransformState;
import interface_adapter.transform.TransformViewModel;
import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

public class ScenePanel extends JPanel implements PropertyChangeListener {

    private final TransformViewModel viewModel;
    private GameObject selectedObject;
    private Runnable onSelectionChangeCallback;
    private Runnable onSceneModifiedCallback;

    public ScenePanel(TransformViewModel viewModel) {
        this.viewModel = viewModel;
    private final TransformViewModel transformViewModel;
    private final TriggerManagerViewModel triggerManagerViewModel;
    private Scene currentScene;
    private GameObject selectedObject;
    private Runnable onSelectionChangeCallback;
    private Runnable onSceneChangeCallback; // Callback for auto-save

    public ScenePanel(TransformViewModel transformViewModel, TriggerManagerViewModel triggerManagerViewModel) {
        this.transformViewModel = transformViewModel;
        this.triggerManagerViewModel = triggerManagerViewModel;
        this.selectedObject = null;

        setBackground(new Color(35, 35, 35));

        this.transformViewModel.addPropertyChangeListener(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    public void setOnSceneModified(Runnable callback) {
        this.onSceneModifiedCallback = callback;
    }

    public void setScene(Scene scene) {
        EditorState.getSceneRepository().setCurrentScene(scene);

        // 1. Remove all visual sprites
        this.removeAll();

        // 2. Re-render game objects belonging to this scene
        if (scene != null && scene.getGameObjects() != null) {
            for (GameObject go : scene.getGameObjects()) {
                go.setActive(true);
            }
        }

        // 3. Refresh Swing layout
        this.revalidate();
        this.repaint();

    // NEW: Callback for Auto-Save
    public void setOnSceneChangeCallback(Runnable callback) {
        this.onSceneChangeCallback = callback;
    }

    public void addSprite(entity.Image image) {
        if (currentScene == null) return; // Guard clause

        try {
            final String id = UUID.randomUUID().toString();
            final String name = image.getName();

            Vector<Double> position = new Vector<>();
            position.add(0.0);
            position.add(0.0);

            Vector<Double> scale = new Vector<>();
            scale.add(1.0);
            scale.add(1.0);

            Transform transform = new Transform(position, 0f, scale);
            SpriteRenderer spriteRenderer = new SpriteRenderer(image, true);
            TriggerManager triggerManager = new TriggerManager();

            GameObject gameObject = new GameObject(id, name, true,
                    new Environment(), transform, spriteRenderer, triggerManager);

            EditorState.getSceneRepository().getCurrentScene().addGameObject(gameObject);

            // notify UI that the scene changed (gameobject added)
            if (onSceneModifiedCallback != null) {
                try {
                    onSceneModifiedCallback.run();
                } catch (Exception ex) {
                    System.err.println("[ScenePanel] onSceneModified callback failed: " + ex.getMessage());
                }
            }

            selectObject(gameObject);

            // log for debugging
            System.out.println("[ScenePanel] Added sprite: " + name + " (Total objects: " + EditorState.getSceneRepository().getCurrentScene().getGameObjects().size() + ")");
            repaint();

            // Trigger Auto-Save
            if (onSceneChangeCallback != null) {
                onSceneChangeCallback.run();
            }
        }
        catch (Exception ex) {
            System.err.println("[ScenePanel] Error adding sprite: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void selectObject(GameObject gameObject) {
        EditorState.setCurrentGameObject(gameObject);
        this.selectedObject = gameObject;
        if (gameObject != null && gameObject.getTransform() != null) {
            Transform transform = gameObject.getTransform();

            transformViewModel.getState().setX(transform.getX());
            transformViewModel.getState().setY(transform.getY());
            transformViewModel.getState().setRotation(transform.getRotation());
            transformViewModel.getState().setScale(transform.getScaleX() * 100.0); // Assuming uniform scale

            transformViewModel.firePropertyChange();

            selectObjectUpdateTriggerManager(gameObject);
        }

        if (onSelectionChangeCallback != null) {
            onSelectionChangeCallback.run();
        }
    }

    private void selectObjectUpdateTriggerManager(GameObject gameObject){

        TriggerManagerState state = triggerManagerViewModel.getState();
        state.clear();

        for (Trigger trigger : gameObject.getTriggerManager().getTriggers()) {

            String event = trigger.getEvent().getEventLabel();
            Map<String, String> eventParameters = trigger.getEvent().getEventParameters();

            List<String> conditions = new ArrayList<>();
            for (Condition condition : trigger.getConditions()) {
                conditions.add(condition.getConditionType());
            }

            List<String> actions = new ArrayList<>();
            for (Action action : trigger.getActions()) {
                actions.add(action.getActionType());
            }

            state.addTrigger(event, eventParameters, conditions, actions);
        }

        triggerManagerViewModel.firePropertyChange();
    }

    public void setOnSelectionChangeCallback(Runnable callback) {
        this.onSelectionChangeCallback = callback;
    }

    public GameObject getSelectedObject() {
        return selectedObject;
    }

    private GameObject findGameObjectByImage(entity.Image image) {
        if (EditorState.getSceneRepository().getCurrentScene().getGameObjects() == null) {
            return null;
        }
        for (GameObject obj : EditorState.getSceneRepository().getCurrentScene().getGameObjects()) {
            SpriteRenderer spriteRenderer = getSpriteRenderer(obj);
            if (spriteRenderer != null && spriteRenderer.getSprite() == image) {
                return obj;
            }
        }
        return null;
    }

    public void addOrSelectSprite(entity.Image image) {
        GameObject existing = findGameObjectByImage(image);
        if (existing != null) {
            selectObject(existing);
            repaint();
        }
        else {
            addSprite(image);
        }
    }

    private void handleMouseClick(int mouseX, int mouseY) {
        if (EditorState.getSceneRepository().getCurrentScene() == null) {
            return;
        }
        for (int i = EditorState.getSceneRepository().getCurrentScene().getGameObjects().size() - 1; i >= 0; i--) {
            GameObject obj = EditorState.getSceneRepository().getCurrentScene().getGameObjects().get(i);
            if (isPointInObject(mouseX, mouseY, obj)) {
                selectObject(obj);
                repaint();
                return;
            }
        }
    }

    private boolean isPointInObject(int x, int y, GameObject obj) {
        Transform transform = obj.getTransform();
        if (transform == null) return false;

        SpriteRenderer spriteRenderer = obj.getSpriteRenderer();
        if (spriteRenderer == null || spriteRenderer.getSprite() == null) return false;

        int spriteW = spriteRenderer.getWidth();
        int spriteH = spriteRenderer.getHeight();

        int drawW = (int) (spriteW * transform.getScaleX());
        int drawH = (int) (spriteH * transform.getScaleY());

        int panelW = getWidth();
        int panelH = getHeight();

        int centerX = (panelW - drawW) / 2;
        int centerY = (panelH - drawH) / 2;

        int drawX = centerX + (int) transform.getX();
        int drawY = centerY - (int) transform.getY();

        return x >= drawX && x <= drawX + drawW && y >= drawY && y <= drawY + drawH;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentScene == null || currentScene.getGameObjects() == null) return;

        if (EditorState.getSceneRepository().getCurrentScene() == null) {
            return;
        }

        if (EditorState.getSceneRepository().getCurrentScene().getGameObjects() == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            int panelW = getWidth();
            int panelH = getHeight();

            // almost same as the old implementation, but i refactored it into separate methods
            for (GameObject obj : EditorState.getSceneRepository().getCurrentScene().getGameObjects()) {
            // Copy list to sort safely
            List<GameObject> sortedObjects = new ArrayList<>(currentScene.getGameObjects());
            sortedObjects.sort((obj1, obj2) -> {
                int z1 = obj1.getSpriteRenderer() != null ? obj1.getSpriteRenderer().getZIndex() : 0;
                int z2 = obj2.getSpriteRenderer() != null ? obj2.getSpriteRenderer().getZIndex() : 0;
                return Integer.compare(z1, z2);
            });

            for (GameObject obj : sortedObjects) {
                renderGameObject(g2, obj, panelW, panelH);
            }

            if (selectedObject != null) {
                updateSelectedObjectTransform();
            }
        }
        finally {
            g2.dispose();
        }
    }

    private void renderGameObject(Graphics2D g2, GameObject obj, int panelW, int panelH) {
        Transform transform = obj.getTransform();
        if (transform == null) return;

        SpriteRenderer spriteRenderer = obj.getSpriteRenderer();
        if (spriteRenderer == null || spriteRenderer.getSprite() == null || !spriteRenderer.getVisible()) return;

        try {
            entity.Image spriteImage = spriteRenderer.getSprite();
            // Safety check for file existence
            java.io.File file = spriteImage.getLocalpath().toFile();
            if (!file.exists()) return;

            Image image = new ImageIcon(file.toString()).getImage();

            float rotationDeg = transform.getRotation();
            int spriteW = spriteRenderer.getWidth();
            int spriteH = spriteRenderer.getHeight();
            int drawW = (int) (spriteW * transform.getScaleX());
            int drawH = (int) (spriteH * transform.getScaleY());

            int centerX = (panelW - drawW) / 2;
            int centerY = (panelH - drawH) / 2;

            int drawX = centerX + (int) transform.getX();
            int drawY = centerY - (int) transform.getY();

            Graphics2D g2Copy = (Graphics2D) g2.create();
            try {
                double theta = Math.toRadians(rotationDeg);
                double pivotX = drawX + drawW / 2.0;
                double pivotY = drawY + drawH / 2.0;

                g2Copy.rotate(theta, pivotX, pivotY);

                float opacity = spriteRenderer.getOpacity() / 100.0f;
                // Clamp opacity
                opacity = Math.max(0f, Math.min(1f, opacity));

                g2Copy.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2Copy.drawImage(image, drawX, drawY, drawW, drawH, this);

                if (obj == selectedObject) {
                    g2Copy.setColor(Color.CYAN);
                    g2Copy.setStroke(new BasicStroke(2));
                    g2Copy.drawRect(drawX, drawY, drawW, drawH);
                }
            }
            finally {
                g2Copy.dispose();
            }
        }
        catch (Exception ex) {
            // Silently fail rendering for this frame
        }
    }

    private void updateSelectedObjectTransform() {
        if (selectedObject == null || selectedObject.getTransform() == null) return;

        TransformState state = transformViewModel.getState();
        Transform transform = selectedObject.getTransform();

        transform.setX(state.getX());
        transform.setY(state.getY());

        double scaleValue = state.getScale() / 100.0;
        transform.setScaleX(scaleValue);
        transform.setScaleY(scaleValue);
        transform.setRotation(state.getRotation());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            updateSelectedObjectTransform();
            repaint();
        }
    }
}