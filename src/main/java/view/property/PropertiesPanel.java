package view.property;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

import interface_adapter.transform.TransformViewModel;
import interface_adapter.transform.TransformController;
import interface_adapter.variable.LocalVariableViewModel;
import interface_adapter.variable.GlobalVariableViewModel;
import interface_adapter.variable.update.UpdateVariableController;
import interface_adapter.variable.delete.DeleteVariableController;
import view.property.trigger.TriggerManagerPanel;

public class PropertiesPanel extends JPanel {

    // Sprite renderer
    private JTextField imageField;

    // Section panels
    private final TransformSectionPanel transformSection;
    private final TriggerManagerPanel triggerManagerPanel;
    private final VariableSectionPanel variableSection;

    public PropertiesPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(45, 45, 45));

        // Outer "Properties" title with white text
        TitledBorder outerBorder = makeSectionBorder("Properties");
        setBorder(BorderFactory.createCompoundBorder(
                outerBorder,
                new EmptyBorder(5, 5, 5, 5)
        ));

        // Initialize section panels
        transformSection = new TransformSectionPanel();
        triggerManagerPanel = new TriggerManagerPanel();
        variableSection = new VariableSectionPanel();

        // Add sections
        add(Box.createVerticalStrut(10));
        add(transformSection);
        add(Box.createVerticalStrut(20));
        add(createSpriteRendererSection());
        add(Box.createVerticalStrut(20));
        add(triggerManagerPanel);
        add(Box.createVerticalStrut(20));
        add(variableSection);
        add(Box.createVerticalGlue());
    }

    private JPanel createSpriteRendererSection() {
        JPanel panel = createSectionPanel("Sprite Renderer");
        GridBagConstraints gbc = baseGbc();

        JLabel imgLabel = createFieldLabel("Image:");
        panel.add(imgLabel, gbc);

        gbc.gridx = 1;
        imageField = new JTextField("bear.png");
        imageField.setBackground(new Color(60, 60, 60));
        imageField.setForeground(Color.WHITE);
        imageField.setCaretColor(Color.WHITE);
        imageField.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 90)));
        imageField.setEditable(false);

        JButton browseButton = new JButton("...");
        browseButton.setMargin(new Insets(0, 4, 0, 4));

        JPanel row = new JPanel();
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.add(imageField);
        row.add(Box.createHorizontalStrut(4));
        row.add(browseButton);

        panel.add(row, gbc);

        return panel;
    }

    // ---------- helper UI methods ----------

    // border creator with white title text
    private TitledBorder makeSectionBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 11),
                Color.WHITE
        );
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(makeSectionBorder(title));
        return panel;
    }

    private GridBagConstraints baseGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        return gbc;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 11f));
        return label;
    }


    public void bind(TransformViewModel viewModel,
                     TransformController controller,
                     Runnable onChangeCallback) {
        transformSection.bind(viewModel, controller, onChangeCallback);
    }

    // NEW: Separate setters for local and global ViewModels
    public void setLocalVariableViewModel(LocalVariableViewModel viewModel) {
        variableSection.setLocalVariableViewModel(viewModel);
    }

    public void setGlobalVariableViewModel(GlobalVariableViewModel viewModel) {
        variableSection.setGlobalVariableViewModel(viewModel);
    }

    public void setVariableController(UpdateVariableController controller) {
        variableSection.setVariableController(controller);
    }

    public void setDeleteVariableController(DeleteVariableController controller) {
        variableSection.setDeleteVariableController(controller);
    }

    public String getSelectedEvent() {
        return null;
    }

    public String getSelectedKey() {
        return null;
    }

    public java.util.List<String> getConditionTexts() {
        return java.util.List.of();
    }

    public java.util.List<String> getActionTexts() {
        return java.util.List.of();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension naturalSize = super.getPreferredSize();

        return new Dimension(300, naturalSize.height);
    }
}