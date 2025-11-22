package view;

import interface_adapter.transform.TransformState;
import interface_adapter.transform.TransformViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

public class ScenePanel extends JPanel implements PropertyChangeListener {

    private final TransformViewModel viewModel;
    private final Image sprite;

    public ScenePanel(TransformViewModel viewModel) {
        this.viewModel = viewModel;

        setBackground(new Color(35, 35, 35));

        URL imgUrl = getClass().getClassLoader().getResource("bear.png");
        if (imgUrl != null) {
            sprite = new ImageIcon(imgUrl).getImage();
            System.out.println("[ScenePanel] Loaded sprite from: " + imgUrl);
        } else {
            sprite = null;
            System.out.println("[ScenePanel] ERROR: Could not find bear.png on classpath");
        }

        // Listen to changes in the view model
        this.viewModel.addPropertyChangeListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (sprite == null) return;

        Graphics2D g2 = (Graphics2D) g.create();
        try {

            TransformState state = viewModel.getState();
            double scale = state.getScale();
            float rotationDeg = state.getRotation();
            int offsetX = (int) state.getX();
            int offsetY = (int) state.getY();

            int panelW = getWidth();
            int panelH = getHeight();

            int spriteW = sprite.getWidth(this);
            int spriteH = sprite.getHeight(this);

            int drawW = (int) (spriteW * scale / 100.0);
            int drawH = (int) (spriteH * scale / 100.0);

            int centerX = (panelW - drawW) / 2;
            int centerY = (panelH - drawH) / 2;

            int drawX = centerX + offsetX;
            int drawY = centerY + offsetY;

            double theta = Math.toRadians(rotationDeg);
            int pivotX = drawX + drawW / 2;
            int pivotY = drawY + drawH / 2;

            g2.rotate(theta, pivotX, pivotY);
            g2.drawImage(sprite, drawX, drawY, drawW, drawH, this);

            g2.setColor(Color.RED);
            g2.drawRect(drawX, drawY, drawW, drawH);
        } finally {
            g2.dispose();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            repaint();
        }
    }
}
