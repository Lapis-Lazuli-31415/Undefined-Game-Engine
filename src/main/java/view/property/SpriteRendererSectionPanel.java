package view.property;

import javax.swing.*;
import java.awt.*;
import view.util.PropertyPanelUtility;

/**
 * Section panel for managing sprite renderer properties.
 * Follows clean architecture by delegating sprite selection to the parent.
 */
public class SpriteRendererSectionPanel extends JPanel {

    private JTextField imageField;
    private JButton browseButton;
    private entity.GameObject currentGameObject;
    private interface_adapter.assets.AssetLibViewModel assetLibViewModel;
    private Runnable onChangeCallback;

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

        browseButton = new JButton("...");
        browseButton.setMargin(new Insets(0, 4, 0, 4));
        browseButton.addActionListener(e -> openSpritePickerDialog());

        JPanel row = new JPanel();
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.add(imageField);
        row.add(Box.createHorizontalStrut(4));
        row.add(browseButton);

        panel.add(row, gbc);

        return panel;
    }

    /**
     * Bind the sprite renderer to a GameObject and AssetLibViewModel.
     * @param gameObject The GameObject whose sprite renderer we're editing
     * @param assetLibViewModel The asset library containing available sprites
     * @param onChangeCallback Callback to invoke when sprite changes
     */
    public void bind(entity.GameObject gameObject,
                     interface_adapter.assets.AssetLibViewModel assetLibViewModel,
                     Runnable onChangeCallback) {
        this.currentGameObject = gameObject;
        this.assetLibViewModel = assetLibViewModel;
        this.onChangeCallback = onChangeCallback;
        updateSpriteRendererUI();
    }

    /**
     * Update the UI to reflect the current sprite.
     */
    private void updateSpriteRendererUI() {
        if (currentGameObject == null || currentGameObject.getSpriteRenderer() == null) {
            imageField.setText("");
            browseButton.setEnabled(false);
            return;
        }

        entity.SpriteRenderer spriteRenderer = currentGameObject.getSpriteRenderer();
        entity.Image sprite = spriteRenderer.getSprite();

        if (sprite != null) {
            imageField.setText(sprite.getName());
        } else {
            imageField.setText("(No sprite)");
        }

        browseButton.setEnabled(true);
    }

    /**
     * Open a dialog to select a sprite from the asset library.
     */
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
            .collect(java.util.stream.Collectors.toList());

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

