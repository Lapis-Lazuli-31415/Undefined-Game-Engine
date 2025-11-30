package view;

import entity.GameObject;
import entity.Scene; // Import Scene
import entity.SpriteRenderer;
import entity.Transform;
import interface_adapter.transform.TransformState;
import interface_adapter.transform.TransformViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

public class ScenePanel extends JPanel implements PropertyChangeListener {

    private final TransformViewModel viewModel;
    // CHANGE: Removed local 'gameObjects' list. We now use the Scene entity.
    private Scene currentScene;
    private GameObject selectedObject;
    private Runnable onSelectionChangeCallback;
    private Runnable onSceneChangeCallback; // Callback for auto-save

    public ScenePanel(TransformViewModel viewModel) {
        this.viewModel = viewModel;
        this.selectedObject = null;

        setBackground(new Color(35, 35, 35));

        this.viewModel.addPropertyChangeListener(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    // NEW: Bind the panel to a Scene entity
    public void setScene(Scene scene) {
        this.currentScene = scene;
        repaint();
    }

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

            GameObject gameObject = new GameObject(id, name, true, new ArrayList<>(), null, spriteRenderer);
            gameObject.setTransform(transform);

            // CHANGE: Add directly to the Scene entity
            currentScene.getGameObjects().add(gameObject);

            selectObject(gameObject);

            System.out.println("[ScenePanel] Added sprite: " + name);
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
        this.selectedObject = gameObject;
        if (gameObject != null && gameObject.getTransform() != null) {
            Transform transform = gameObject.getTransform();

            viewModel.getState().setX(transform.getX());
            viewModel.getState().setY(transform.getY());
            viewModel.getState().setRotation(transform.getRotation());
            viewModel.getState().setScale(transform.getScaleX() * 100.0); // Assuming uniform scale

            viewModel.firePropertyChange();
        }

        if (onSelectionChangeCallback != null) {
            onSelectionChangeCallback.run();
        }
    }

    public void setOnSelectionChangeCallback(Runnable callback) {
        this.onSelectionChangeCallback = callback;
    }

    public GameObject getSelectedObject() {
        return selectedObject;
    }

    private GameObject findGameObjectByImage(entity.Image image) {
        if (currentScene == null || currentScene.getGameObjects() == null) return null;

        for (GameObject obj : currentScene.getGameObjects()) {
            SpriteRenderer spriteRenderer = obj.getSpriteRenderer();
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
        if (currentScene == null || currentScene.getGameObjects() == null) return;

        List<GameObject> objects = currentScene.getGameObjects();
        // Iterate backwards to select top-most object first
        for (int i = objects.size() - 1; i >= 0; i--) {
            GameObject obj = objects.get(i);
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

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            int panelW = getWidth();
            int panelH = getHeight();

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

        TransformState state = viewModel.getState();
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