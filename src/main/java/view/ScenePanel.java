package view;

import interface_adapter.transform.TransformViewModel;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ScenePanel extends JPanel {

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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (sprite == null) return;

        // We need Graphics2D for rotation
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            // --- 1. Read from view model ---
            double scale = viewModel.getScale();      // assuming this is like 100 = 100%
            float rotationDeg = viewModel.getRotation();

            int panelW = getWidth();
            int panelH = getHeight();

            int spriteW = sprite.getWidth(this);
            int spriteH = sprite.getHeight(this);

            // --- 2. Scaled size ---
            int drawW = (int) (spriteW * scale / 100.0);
            int drawH = (int) (spriteH * scale / 100.0);

            // --- 3. Base position: centered + offsets from view model ---
            int centerX = (panelW - drawW) / 2;
            int centerY = (panelH - drawH) / 2;

            int offsetX = (int) viewModel.getX();
            int offsetY = (int) viewModel.getY();

            int drawX = centerX + offsetX;
            int drawY = centerY + offsetY;

            // --- 4. Apply rotation around the sprite’s center ---
            double theta = Math.toRadians(rotationDeg);

            int pivotX = drawX + drawW / 2;
            int pivotY = drawY + drawH / 2;

            g2.rotate(theta, pivotX, pivotY);

            // --- 5. Draw image (under rotation) ---
            g2.drawImage(sprite, drawX, drawY, drawW, drawH, this);

            // Optional: draw the bounding box so you can see the rotated bounds
            g2.setColor(Color.RED);
            g2.drawRect(drawX, drawY, drawW, drawH);
        } finally {
            g2.dispose(); // restore original Graphics
        }
    }
}
