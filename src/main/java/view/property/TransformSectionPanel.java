package view.property;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import interface_adapter.transform.TransformState;
import interface_adapter.transform.TransformViewModel;
import interface_adapter.transform.TransformController;
import view.util.PropertyPanelUtility;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TransformSectionPanel extends JPanel implements PropertyChangeListener {

    private static final double DEFAULT_X = 0.0;
    private static final double DEFAULT_Y = 0.0;
    private static final double DEFAULT_SCALE = 1.0;
    private static final float DEFAULT_ROTATION = 0.0f;

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

    public TransformSectionPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel contentPanel = createTransformSection();
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createTransformSection() {
        JPanel panel = PropertyPanelUtility.createSectionPanel("Transform");
        GridBagConstraints gbc = PropertyPanelUtility.baseGbc();

        // Position row
        JLabel posLabel = PropertyPanelUtility.createFieldLabel("Position:");
        panel.add(posLabel, gbc);

        gbc.gridx = 1;

        JPanel posRow = new JPanel();
        posRow.setOpaque(false);
        posRow.setLayout(new BoxLayout(posRow, BoxLayout.X_AXIS));

        JLabel xLabel = PropertyPanelUtility.createInlineLabel("X:");
        JLabel yLabel = PropertyPanelUtility.createInlineLabel("Y:");

        posXField = PropertyPanelUtility.smallField(String.valueOf(DEFAULT_X));
        posYField = PropertyPanelUtility.smallField(String.valueOf(DEFAULT_Y));

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
        JLabel rotLabel = PropertyPanelUtility.createFieldLabel("Rotation:");
        panel.add(rotLabel, gbc);

        gbc.gridx = 1;
        rotationField = PropertyPanelUtility.smallField(String.valueOf(DEFAULT_ROTATION));
        panel.add(rotationField, gbc);

        // Scale row
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel scaleLabel = PropertyPanelUtility.createFieldLabel("Scale:");
        panel.add(scaleLabel, gbc);

        gbc.gridx = 1;
        scaleField = PropertyPanelUtility.smallField(String.valueOf(DEFAULT_SCALE));
        panel.add(scaleField, gbc);

        attachTransformListeners();

        return panel;
    }

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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            // Defer the UI update until after the current event finishes to avoid
            // "Attempt to mutate in notification"
            SwingUtilities.invokeLater(this::syncFromViewModel);
        }
    }
}