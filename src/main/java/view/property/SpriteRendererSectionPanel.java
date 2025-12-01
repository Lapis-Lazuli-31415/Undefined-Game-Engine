package view.property;

import javax.swing.*;
import java.awt.*;
import view.util.PropertyPanelUtility;

public class SpriteRendererSectionPanel extends JPanel {

    private JTextField imageField;
    private JTextField zIndexField;
    private JButton browseButton;
    private entity.GameObject currentGameObject;
    private interface_adapter.assets.AssetLibViewModel assetLibViewModel;
    private Runnable onChangeCallback;
    private boolean isUpdatingUI = false;

    public SpriteRendererSectionPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel contentPanel = createSpriteRendererSection();
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createSpriteRendererSection() {
        JPanel panel = PropertyPanelUtility.createSectionPanel("Sprite Renderer");
        GridBagConstraints gbc = PropertyPanelUtility.baseGbc();

        JLabel imgLabel = PropertyPanelUtility.createFieldLabel("Image:");
        panel.add(imgLabel, gbc);

        gbc.gridx = 1;
        imageField = new JTextField();
        imageField.setBackground(new Color(60, 60, 60));
        imageField.setForeground(Color.WHITE);
        imageField.setCaretColor(Color.WHITE);
        imageField.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 90)));
        imageField.setEditable(false);

        browseButton = PropertyPanelUtility.createEditButton();
        browseButton.addActionListener(e -> openSpritePickerDialog());

        JPanel row = new JPanel(new BorderLayout(4, 0));
        row.setOpaque(false);

        row.add(imageField, BorderLayout.CENTER);
        row.add(browseButton, BorderLayout.EAST);

        panel.add(row, gbc);

        // z-index
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel zIndexLabel = PropertyPanelUtility.createFieldLabel("Z-Index:");
        panel.add(zIndexLabel, gbc);

        gbc.gridx = 1;
        zIndexField = PropertyPanelUtility.smallField("0");
        panel.add(zIndexField, gbc);

        attachZIndexListener();

        return panel;
    }

    private void attachZIndexListener() {
        zIndexField.addActionListener(actionEvent -> updateZIndexFromField());
        zIndexField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent focusEvent) {
                updateZIndexFromField();
            }
        });
    }

    private void updateZIndexFromField() {
        if (isUpdatingUI || currentGameObject == null || currentGameObject.getSpriteRenderer() == null) {
            return;
        }

        try {
            int newZIndex = Integer.parseInt(zIndexField.getText().trim());
            entity.SpriteRenderer spriteRenderer = currentGameObject.getSpriteRenderer();
            spriteRenderer.setZIndex(newZIndex);

            if (onChangeCallback != null) {
                onChangeCallback.run();
            }
        } catch (NumberFormatException numberFormatException) {
            if (currentGameObject.getSpriteRenderer() != null) {
                zIndexField.setText(String.valueOf(currentGameObject.getSpriteRenderer().getZIndex()));
            }
        }
    }

    public void bind(entity.GameObject gameObject,
                     interface_adapter.assets.AssetLibViewModel assetLibViewModel,
                     Runnable onChangeCallback) {
        this.currentGameObject = gameObject;
        this.assetLibViewModel = assetLibViewModel;
        this.onChangeCallback = onChangeCallback;
        updateSpriteRendererUI();
    }

    private void updateSpriteRendererUI() {
        isUpdatingUI = true;
        try {
            if (currentGameObject == null || currentGameObject.getSpriteRenderer() == null) {
                imageField.setText("");
                zIndexField.setText("0");
                browseButton.setEnabled(false);
                zIndexField.setEnabled(false);
                return;
            }

            entity.SpriteRenderer spriteRenderer = currentGameObject.getSpriteRenderer();
            entity.Image sprite = spriteRenderer.getSprite();

            if (sprite != null) {
                imageField.setText(sprite.getName());
            } else {
                imageField.setText("(No sprite)");
            }

            zIndexField.setText(String.valueOf(spriteRenderer.getZIndex()));
            browseButton.setEnabled(true);
            zIndexField.setEnabled(true);
        } finally {
            isUpdatingUI = false;
        }
    }

    private void openSpritePickerDialog() {
        if (currentGameObject == null || assetLibViewModel == null) {
            JOptionPane.showMessageDialog(this,
                "No GameObject selected",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.util.List<entity.Image> availableSprites = assetLibViewModel.getAssetLib().getAll().stream()
            .filter(asset -> asset instanceof entity.Image)
            .map(asset -> (entity.Image) asset)
            .toList();

        if (availableSprites.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No sprites available in the asset library. Please import a sprite first.",
                "No Sprites",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] spriteNames = availableSprites.stream()
            .map(entity.Asset::getName)
            .toArray(String[]::new);

        String selectedName = (String) JOptionPane.showInputDialog(
            this,
            "Select a sprite:",
            "Change Sprite",
            JOptionPane.PLAIN_MESSAGE,
            null,
            spriteNames,
            spriteNames[0]
        );

        if (selectedName != null) {
            entity.Image selectedSprite = availableSprites.stream()
                .filter(img -> img.getName().equals(selectedName))
                .findFirst()
                .orElse(null);

            if (selectedSprite != null) {
                entity.SpriteRenderer spriteRenderer = currentGameObject.getSpriteRenderer();
                if (spriteRenderer == null) {
                    spriteRenderer = new entity.SpriteRenderer(selectedSprite, true);
                    currentGameObject.setSpriteRenderer(spriteRenderer);
                } else {
                    entity.SpriteRenderer newRenderer = new entity.SpriteRenderer(
                        selectedSprite,
                        spriteRenderer.getVisible()
                    );
                    newRenderer.setOpacity(spriteRenderer.getOpacity());
                    newRenderer.setZIndex(spriteRenderer.getZIndex());
                    currentGameObject.setSpriteRenderer(newRenderer);
                }

                updateSpriteRendererUI();

                if (onChangeCallback != null) {
                    onChangeCallback.run();
                }
            }
        }
    }
}

