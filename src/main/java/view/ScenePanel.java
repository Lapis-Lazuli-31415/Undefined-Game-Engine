package view;

import entity.GameObject;
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
    private final List<GameObject> gameObjects;
    private GameObject selectedObject;
    private Runnable onSelectionChangeCallback;

    public ScenePanel(TransformViewModel viewModel) {
        this.viewModel = viewModel;
        this.gameObjects = new ArrayList<>();
        this.selectedObject = null;

        setBackground(new Color(35, 35, 35));

        // Listen to changes in the view model
        this.viewModel.addPropertyChangeListener(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    public void addSprite(entity.Image image) {
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

            // not sure if i should assign environment = null, i only did that as a filler
            GameObject gameObject = new GameObject(id, name, true, new ArrayList<>(), null,
                    spriteRenderer);
            gameObject.setTransform(transform);

            gameObjects.add(gameObject);

            selectObject(gameObject);

            // log for debugging
            System.out.println("[ScenePanel] Added sprite: " + name + " (Total objects: " + gameObjects.size() + ")");
            repaint();
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
            viewModel.getState().setScale(transform.getScaleX() * 100.0);

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
        for (GameObject obj : gameObjects) {
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
            // sprite isnt added yet
            addSprite(image);
        }
    }

    private void handleMouseClick(int mouseX, int mouseY) {
        for (int i = gameObjects.size() - 1; i >= 0; i--) {
            GameObject obj = gameObjects.get(i);
            if (isPointInObject(mouseX, mouseY, obj)) {
                selectObject(obj);
                repaint();
                return;
            }
        }
    }

    private boolean isPointInObject(int x, int y, GameObject obj) {
        Transform transform = obj.getTransform();
        if (transform == null) {
            return false;
        }

        SpriteRenderer spriteRenderer = obj.getSpriteRenderer();
        if (spriteRenderer == null || spriteRenderer.getSprite() == null) {
            return false;
        }

        int spriteW = spriteRenderer.getWidth();
        int spriteH = spriteRenderer.getHeight();

        int drawW = (int) (spriteW * transform.getScaleX());
        int drawH = (int) (spriteH * transform.getScaleY());

        int panelW = getWidth();
        int panelH = getHeight();

        int centerX = (panelW - drawW) / 2;
        int centerY = (panelH - drawH) / 2;

        int drawX = centerX + (int) transform.getX();
        int drawY = centerY + (int) transform.getY();

        return x >= drawX && x <= drawX + drawW && y >= drawY && y <= drawY + drawH;
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            int panelW = getWidth();
            int panelH = getHeight();

            // almost same as the old implementation, but i refactored it into separate methods
            for (GameObject obj : gameObjects) {
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
        if (transform == null) {
            return;
        }

        SpriteRenderer spriteRenderer = obj.getSpriteRenderer();
        if (spriteRenderer == null || spriteRenderer.getSprite() == null || !spriteRenderer.getVisible()) {
            return;
        }

        try {
            entity.Image spriteImage = spriteRenderer.getSprite();
            String filePath = spriteImage.getLocalpath().toString();
            Image image = new ImageIcon(filePath).getImage();

            if (image == null) {
                return;
            }

            float rotationDeg = transform.getRotation();

            int spriteW = spriteRenderer.getWidth();
            int spriteH = spriteRenderer.getHeight();

            int drawW = (int) (spriteW * transform.getScaleX());
            int drawH = (int) (spriteH * transform.getScaleY());

            int centerX = (panelW - drawW) / 2;
            int centerY = (panelH - drawH) / 2;

            int drawX = centerX + (int) transform.getX();
            int drawY = centerY + (int) transform.getY();

            Graphics2D g2Copy = (Graphics2D) g2.create();
            try {
                double theta = Math.toRadians(rotationDeg);
                double pivotX = drawX + drawW / 2;
                double pivotY = drawY + drawH / 2;

                g2Copy.rotate(theta, pivotX, pivotY);

                float opacity = spriteRenderer.getOpacity() / 100.0f;
                g2Copy.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

                g2Copy.drawImage(image, drawX, drawY, drawW, drawH, this);

                // selection outline
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
            System.err.println("[ScenePanel] Error rendering object: " + ex.getMessage());
        }
    }

    private void updateSelectedObjectTransform() {
        if (selectedObject == null || selectedObject.getTransform() == null) {
            return;
        }

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

