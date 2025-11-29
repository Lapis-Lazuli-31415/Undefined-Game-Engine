package view.property.trigger;

import app.use_case_factory.TriggerUseCaseFactory;
import interface_adapter.trigger.condition.ConditionEditorState;
import interface_adapter.trigger.condition.ConditionEditorViewModel;
import interface_adapter.trigger.condition.edit.ConditionEditSaveController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ConditionEditorDialog extends JDialog implements PropertyChangeListener {

    private final JTextArea scriptArea;

    // Callback for when 'Save' is clicked
    private final ConditionEditorViewModel conditionEditorViewModel;
    private final ConditionEditSaveController conditionEditSaveController;
    private final int triggerIndex;
    private final int conditionIndex;

    public ConditionEditorDialog(Window owner,
                                 int triggerIndex,
                                 int conditionIndex,
                                 String initialScript,
                                 ConditionEditorViewModel conditionEditorViewModel,
                                 TriggerUseCaseFactory triggerUseCaseFactory) {
        super(owner, "Edit Condition", ModalityType.APPLICATION_MODAL);
        this.triggerIndex = triggerIndex;
        this.conditionIndex = conditionIndex;
        this.conditionEditSaveController =
                triggerUseCaseFactory.createConditionEditSaveController();
        this.conditionEditorViewModel = conditionEditorViewModel;

        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(45, 45, 45));

        // --- 1. Header ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 10, 0, 10));

        JLabel titleLabel = new JLabel("Condition");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // --- 2. Script Input Area (Center) ---
        scriptArea = new JTextArea(initialScript);
        scriptArea.setFont(new Font("Consolas", Font.PLAIN, 12)); // Monospaced for code
        scriptArea.setBackground(new Color(60, 60, 60));
        scriptArea.setForeground(Color.WHITE);
        scriptArea.setCaretColor(Color.WHITE);
        scriptArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        scriptArea.setLineWrap(true);
        scriptArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(scriptArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 90)));
        // Add padding around the scroll pane
        JPanel scrollWrapper = new JPanel(new BorderLayout());
        scrollWrapper.setOpaque(false);
        scrollWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
        scrollWrapper.add(scrollPane, BorderLayout.CENTER);

        add(scrollWrapper, BorderLayout.CENTER);

        // --- 3. Footer (Buttons) ---
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        // Left side: Documentation Button
        JButton docsBtn = new JButton("Documentation");
        styleButton(docsBtn);
        docsBtn.addActionListener(e -> showDocumentation());

        // Right side: Save/Cancel
        JPanel actionButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        actionButtons.setOpaque(false);

        JButton saveBtn = new JButton("Save");
        styleButton(saveBtn);
        // Save Action: Execute Controller
        saveBtn.addActionListener(e -> {
            String script = scriptArea.getText();
            // Execute logic. DO NOT dispose here. Wait for propertyChange.
            conditionEditSaveController.execute(triggerIndex, conditionIndex, script);
        });

        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn);
        cancelBtn.addActionListener(e -> dispose());

        actionButtons.add(saveBtn);
        actionButtons.add(cancelBtn);

        footerPanel.add(docsBtn, BorderLayout.WEST);
        footerPanel.add(actionButtons, BorderLayout.EAST);

        add(footerPanel, BorderLayout.SOUTH);

        setSize(500, 400);
        setLocationRelativeTo(owner);

        this.conditionEditorViewModel.addPropertyChangeListener(this);
    }

    private void showDocumentation() {
        // Simple popup for docs, or open a URL
        JTextArea docText = new JTextArea(
                "Available Variables:\n" +
                        "- hp: Player Health (Integer)\n" +
                        "- x: Player X Position (Double)\n" +
                        "- isAngry: Boolean Flag\n\n" +
                        "Examples:\n" +
                        "hp > 0 && x < 100\n" +
                        "isAngry == true"
        );
        docText.setEditable(false);
        docText.setBackground(new Color(60, 60, 60));
        docText.setForeground(Color.WHITE);

        JOptionPane.showMessageDialog(this, new JScrollPane(docText), "Script Documentation", JOptionPane.INFORMATION_MESSAGE);
    }

    private void styleButton(JButton btn) {
        btn.setUI(new BasicButtonUI());
        btn.setBackground(new Color(60, 60, 60));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 90, 90)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ConditionEditorState state = (ConditionEditorState) evt.getNewValue();

        if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
            // FAILURE: Show Error Panel
            JOptionPane.showMessageDialog(this,
                    state.getErrorMessage(),
                    "Save Failed",
                    JOptionPane.ERROR_MESSAGE);
            // Dialog stays open so user can fix the script
        } else {
            // SUCCESS: Show Notification and Close
            JOptionPane.showMessageDialog(this,
                    "Condition saved successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            conditionEditorViewModel.removePropertyChangeListener(this);
            dispose(); // Close window only on success
        }
    }
}
