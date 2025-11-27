package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

import interface_adapter.transform.TransformState;
import interface_adapter.transform.TransformViewModel;
import interface_adapter.transform.TransformController;
import view.property.trigger.TriggerManagerPanel;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PropertiesPanel extends JPanel implements PropertyChangeListener {

    private static final double DEFAULT_X = 0.0;
    private static final double DEFAULT_Y = 0.0;
    private static final double DEFAULT_SCALE = 1.0;
    private static final float  DEFAULT_ROTATION = 0.0f;

    // Transform fields
    private JTextField posXField;
    private JTextField posYField;
    private JTextField rotationField;
    private JTextField scaleField;

    // View model / Controller
    private TransformViewModel viewModel;
    private TransformController controller;
    private Runnable onChangeCallback;

    // Prevent recursion when we update the UI from the model
    private boolean isUpdatingUI = false;

    // Sprite renderer
    private JTextField imageField;

    // Trigger
    private JComboBox<String> eventCombo;
    private JTextField keyField;

    // Trigger / scripting models
    private DefaultListModel<String> conditionsModel;
    private JList<String> conditionsList;

    private DefaultListModel<String> actionsModel;
    private JList<String> actionsList;

    // Variables
    private JTextField intNameField;
    private JTextField intValueField;
    private JTextField boolNameField;
    private JCheckBox boolValueCheck;

    private final TriggerManagerPanel triggerManagerPanel;

    public PropertiesPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(260, 700));
        setBackground(new Color(45, 45, 45));

        // Outer "Properties" title with white text
        TitledBorder outerBorder = makeSectionBorder("Properties");
        setBorder(BorderFactory.createCompoundBorder(
                outerBorder,
                new EmptyBorder(5, 5, 5, 5)
        ));

        triggerManagerPanel = new TriggerManagerPanel();

        add(createTransformSection());
        add(Box.createVerticalStrut(10));
        add(createSpriteRendererSection());
        add(Box.createVerticalStrut(10));
        add(triggerManagerPanel);
        add(Box.createVerticalStrut(10));
        add(createVariableSection());
        add(Box.createVerticalGlue());
    }

    private JPanel createTransformSection() {
        JPanel panel = createSectionPanel("Transform");
        GridBagConstraints gbc = baseGbc();

        // Position row
        JLabel posLabel = createFieldLabel("Position:");
        panel.add(posLabel, gbc);

        gbc.gridx = 1;

        JPanel posRow = new JPanel();
        posRow.setOpaque(false);
        posRow.setLayout(new BoxLayout(posRow, BoxLayout.X_AXIS));

        JLabel xLabel = createInlineLabel("X:");
        JLabel yLabel = createInlineLabel("Y:");

        posXField = smallField(String.valueOf(DEFAULT_X));
        posYField = smallField(String.valueOf(DEFAULT_Y));

        posRow.add(xLabel);
        posRow.add(Box.createHorizontalStrut(3));
        posRow.add(posXField);
        posRow.add(Box.createHorizontalStrut(6));
        posRow.add(yLabel);
        posRow.add(Box.createHorizontalStrut(3));
        posRow.add(posYField);

        posRow.setMaximumSize(posRow.getPreferredSize());
        panel.add(posRow, gbc);

        // Rotation row
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel rotLabel = createFieldLabel("Rotation:");
        panel.add(rotLabel, gbc);

        gbc.gridx = 1;
        rotationField = smallField(String.valueOf(DEFAULT_ROTATION));
        panel.add(rotationField, gbc);

        // Scale row
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel scaleLabel = createFieldLabel("Scale:");
        panel.add(scaleLabel, gbc);

        gbc.gridx = 1;
        scaleField = smallField(String.valueOf(DEFAULT_SCALE));
        panel.add(scaleField, gbc);

        attachTransformListeners();

        return panel;
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

    private JPanel createTriggerSection() {
        JPanel panel = createSectionPanel("Trigger");
        GridBagConstraints gbc = baseGbc();

        //  EVENT DROPDOWN
        JLabel eventLabel = createFieldLabel("Event:");
        panel.add(eventLabel, gbc);

        gbc.gridx = 1;
        eventCombo = new JComboBox<>(new String[]{"OnClick", "OnKey"});
        styleCombo(eventCombo);
        panel.add(eventCombo, gbc);

        //  KEY FIELD (for OnKey)
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel keyLabel = createFieldLabel("Key:");
        panel.add(keyLabel, gbc);

        gbc.gridx = 1;
        keyField = smallField("Space");  // default example
        panel.add(keyField, gbc);

        //  CONDITIONS LIST
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel condLabel = createFieldLabel("Conditions:");
        panel.add(condLabel, gbc);

        gbc.gridy++;
        conditionsModel = new DefaultListModel<>();
        conditionsList = new JList<>(conditionsModel);
        conditionsList.setBackground(new Color(40, 40, 40));
        conditionsList.setForeground(Color.WHITE);

        JScrollPane condScroll = new JScrollPane(conditionsList);
        condScroll.setPreferredSize(new Dimension(200, 80));
        panel.add(condScroll, gbc);

        // Condition buttons (Add / Edit / Remove)
        gbc.gridy++;
        JPanel condButtons = new JPanel();
        condButtons.setOpaque(false);
        condButtons.setLayout(new BoxLayout(condButtons, BoxLayout.X_AXIS));

        JButton addCondBtn = new JButton("Add");
        JButton editCondBtn = new JButton("Edit");
        JButton removeCondBtn = new JButton("Remove");

        condButtons.add(addCondBtn);
        condButtons.add(Box.createHorizontalStrut(4));
        condButtons.add(editCondBtn);
        condButtons.add(Box.createHorizontalStrut(4));
        condButtons.add(removeCondBtn);

        panel.add(condButtons, gbc);

        // Conditions Listener
        addCondBtn.addActionListener(e -> {
            String expr = JOptionPane.showInputDialog(
                    this,
                    "Enter condition (e.g., hp > 5 or (x + 1) - 25 > 0):",
                    "Add Condition",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (expr != null && !expr.isBlank()) {
                conditionsModel.addElement(expr.trim());
            }
        });

        editCondBtn.addActionListener(e -> {
            int idx = conditionsList.getSelectedIndex();
            if (idx >= 0) {
                String current = conditionsModel.getElementAt(idx);
                String expr = JOptionPane.showInputDialog(
                        this,
                        "Edit condition:",
                        current
                );
                if (expr != null && !expr.isBlank()) {
                    conditionsModel.set(idx, expr.trim());
                }
            }
        });

        removeCondBtn.addActionListener(e -> {
            int idx = conditionsList.getSelectedIndex();
            if (idx >= 0) {
                conditionsModel.remove(idx);
            }
        });

        // Actions List
        gbc.gridy++;
        JLabel actionsLabel = createFieldLabel("Actions:");
        panel.add(actionsLabel, gbc);

        gbc.gridy++;
        actionsModel = new DefaultListModel<>();
        actionsList = new JList<>(actionsModel);
        actionsList.setBackground(new Color(40, 40, 40));
        actionsList.setForeground(Color.WHITE);

        JScrollPane actionsScroll = new JScrollPane(actionsList);
        actionsScroll.setPreferredSize(new Dimension(200, 80));
        panel.add(actionsScroll, gbc);

        // Action buttons
        gbc.gridy++;
        JPanel actionsButtons = new JPanel();
        actionsButtons.setOpaque(false);
        actionsButtons.setLayout(new BoxLayout(actionsButtons, BoxLayout.X_AXIS));

        JButton addActionBtn = new JButton("Add");
        JButton editActionBtn = new JButton("Edit");
        JButton removeActionBtn = new JButton("Remove");

        actionsButtons.add(addActionBtn);
        actionsButtons.add(Box.createHorizontalStrut(4));
        actionsButtons.add(editActionBtn);
        actionsButtons.add(Box.createHorizontalStrut(4));
        actionsButtons.add(removeActionBtn);

        panel.add(actionsButtons, gbc);

        // Actions Listener
        addActionBtn.addActionListener(e -> {
            String action = JOptionPane.showInputDialog(
                    this,
                    "Enter action (e.g., Change Sprite to bear.png):",
                    "Add Action",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (action != null && !action.isBlank()) {
                actionsModel.addElement(action.trim());
            }
        });

        editActionBtn.addActionListener(e -> {
            int idx = actionsList.getSelectedIndex();
            if (idx >= 0) {
                String current = actionsModel.getElementAt(idx);
                String action = JOptionPane.showInputDialog(
                        this,
                        "Edit action:",
                        current
                );
                if (action != null && !action.isBlank()) {
                    actionsModel.set(idx, action.trim());
                }
            }
        });

        removeActionBtn.addActionListener(e -> {
            int idx = actionsList.getSelectedIndex();
            if (idx >= 0) {
                actionsModel.remove(idx);
            }
        });

        return panel;
    }


    private JPanel createVariableSection() {
        JPanel panel = createSectionPanel("Variable");
        GridBagConstraints gbc = baseGbc();

        // Integers header
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel intHeader = createSubHeaderLabel("Integers:");
        panel.add(intHeader, gbc);

        // Integer row - Name
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel nameLabel = createFieldLabel("Name");
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        intNameField = smallField("HP");
        panel.add(intNameField, gbc);

        // Integer row - Value
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel valueLabel = createFieldLabel("Value");
        panel.add(valueLabel, gbc);

        gbc.gridx = 1;
        intValueField = smallField("100");
        panel.add(intValueField, gbc);

        // Spacer before booleans
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(Box.createVerticalStrut(12), gbc);

        // Booleans header
        gbc.gridy++;
        JLabel boolHeader = createSubHeaderLabel("Booleans:");
        panel.add(boolHeader, gbc);

        // Boolean row - Name
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel boolNameLabel = createFieldLabel("Name");
        panel.add(boolNameLabel, gbc);

        gbc.gridx = 1;
        boolNameField = smallField("isAngry");
        panel.add(boolNameField, gbc);

        // Boolean row - Value
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel boolValueLabel = createFieldLabel("Value");
        panel.add(boolValueLabel, gbc);

        gbc.gridx = 1;
        boolValueCheck = new JCheckBox("true");
        boolValueCheck.setOpaque(false);
        boolValueCheck.setForeground(Color.WHITE);
        panel.add(boolValueCheck, gbc);

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
        label.setForeground(Color.WHITE);        // all main labels white
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 11f));
        return label;
    }

    // Small inline labels like "X:" / "Y:"
    private JLabel createInlineLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);        // make them visible
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 11f));
        return label;
    }

    private JLabel createSubHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 11f));
        return label;
    }

    private JTextField smallField(String defaultText) {
        JTextField field = new JTextField(defaultText);

        // keep these boxes narrow
        Dimension size = new Dimension(60, 22);
        field.setPreferredSize(size);
        field.setMinimumSize(size);
        field.setMaximumSize(size);

        field.setBackground(new Color(60, 60, 60));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 90)));
        return field;
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setBackground(new Color(60, 60, 60));
        combo.setForeground(Color.WHITE);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
    }

    // --- Public API for binding from HomeView ---

    public void bind(TransformViewModel viewModel,
                     TransformController controller,
                     Runnable onChangeCallback) {

        if (this.viewModel != null) {
            this.viewModel.removePropertyChangeListener(this);
        }
        this.viewModel = viewModel;
        this.controller = controller;
        this.onChangeCallback = onChangeCallback;

        if (viewModel == null) {
            isUpdatingUI = true;
            posXField.setText("");
            posYField.setText("");
            rotationField.setText("");
            scaleField.setText("");
            isUpdatingUI = false;
            return;
        }

        // Listen to VM changes
        this.viewModel.addPropertyChangeListener(this);

        // Initial sync FROM VM TO UI
        syncFromViewModel();
    }

    private void syncFromViewModel() {
        if (viewModel == null) return;

        TransformState s = viewModel.getState();

        isUpdatingUI = true;
        try {
            posXField.setText(String.valueOf(s.getX()));
            posYField.setText(String.valueOf(s.getY()));
            rotationField.setText(String.valueOf(s.getRotation()));
            scaleField.setText(String.valueOf(s.getScale()));
        } finally {
            isUpdatingUI = false;
        }
    }

    private void attachTransformListeners() {
        // When a field loses focus, push changes into the use case.
        FocusAdapter focusAdapter = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateTransformFromFields();
            }
        };

        // When user presses Enter in a field, also commit.
        ActionListener actionListener = e -> updateTransformFromFields();

        posXField.addFocusListener(focusAdapter);
        posYField.addFocusListener(focusAdapter);
        rotationField.addFocusListener(focusAdapter);
        scaleField.addFocusListener(focusAdapter);

        posXField.addActionListener(actionListener);
        posYField.addActionListener(actionListener);
        rotationField.addActionListener(actionListener);
        scaleField.addActionListener(actionListener);
    }

    private void updateTransformFromFields() {
        if (viewModel == null || controller == null) {
            return;
        }

        // no triggering another update if in the middle of syncFromViewModel
        if (isUpdatingUI) {
            return;
        }
        try {
            double x = Double.parseDouble(posXField.getText());
            double y = Double.parseDouble(posYField.getText());
            float rot = Float.parseFloat(rotationField.getText());
            double scale = Double.parseDouble(scaleField.getText());

            controller.updateTransform(x, y, scale, rot);

            if (onChangeCallback != null) {
                onChangeCallback.run();
            }
        } catch (NumberFormatException ex) {
            // invalid input → reset everything to defaults
            resetToDefaults();
        }
    }

    private void resetToDefaults() {
        isUpdatingUI = true;
        try {
            posXField.setText(String.valueOf(DEFAULT_X));
            posYField.setText(String.valueOf(DEFAULT_Y));
            rotationField.setText(String.valueOf(DEFAULT_ROTATION));
            scaleField.setText(String.valueOf(DEFAULT_SCALE));
        } finally {
            isUpdatingUI = false;
        }

        // push defaults through the controller so entity & ViewModel match
        if (controller != null) {
            controller.updateTransform(DEFAULT_X, DEFAULT_Y, DEFAULT_SCALE, DEFAULT_ROTATION);
            if (onChangeCallback != null) {
                onChangeCallback.run();
            }
        }
    }


    //  Trigger getters

    public String getSelectedEvent() {
        return eventCombo != null && eventCombo.getSelectedItem() != null
                ? eventCombo.getSelectedItem().toString()   // "OnClick" / "OnKey"
                : null;
    }

    public String getSelectedKey() {
        return keyField != null ? keyField.getText() : null;  // e.g. "Space"
    }

    public java.util.List<String> getConditionTexts() {
        if (conditionsModel == null) return java.util.List.of();
        return java.util.Collections.list(conditionsModel.elements());
    }

    public java.util.List<String> getActionTexts() {
        if (actionsModel == null) return java.util.List.of();
        return java.util.Collections.list(actionsModel.elements());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            // Defer the UI update until after the current event finishes to avoid
            // "Attempt to mutate in notification"
            SwingUtilities.invokeLater(this::syncFromViewModel);
        }
    }



}
