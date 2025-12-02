package view.property.trigger;

import app.use_case_factory.TriggerUseCaseFactory;
import interface_adapter.trigger.condition.ConditionEditorState;
import interface_adapter.trigger.condition.ConditionEditorViewModel;
import interface_adapter.trigger.condition.edit.ConditionEditSaveController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ConditionEditorDialog extends JDialog implements PropertyChangeListener {

    private final JTextArea scriptArea;

    // Callback for when 'Save' is clicked
    private final ConditionEditorViewModel conditionEditorViewModel;
    private final ConditionEditSaveController conditionEditSaveController;

    // NEW: Callback for auto-save
    private final Runnable onChangeCallback;

    public ConditionEditorDialog(Window owner,
                                 int triggerIndex,
                                 int conditionIndex,
                                 String initialScript,
                                 ConditionEditorViewModel conditionEditorViewModel,
                                 TriggerUseCaseFactory triggerUseCaseFactory,
                                 Runnable onChangeCallback) {
        super(owner, "Condition Editor", ModalityType.APPLICATION_MODAL);
        this.conditionEditSaveController =
                triggerUseCaseFactory.createConditionEditSaveController();
        this.conditionEditorViewModel = conditionEditorViewModel;

        this.conditionEditorViewModel.addPropertyChangeListener(this);

        this.onChangeCallback = onChangeCallback;

        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(45, 45, 45));

        // --- 1. Header ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 10, 0, 10));

        JLabel titleLabel = new JLabel("Condition Script");
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
        cancelBtn.addActionListener(e -> {
                conditionEditorViewModel.removePropertyChangeListener(this);
                dispose();}
        );

        actionButtons.add(saveBtn);
        actionButtons.add(cancelBtn);

        footerPanel.add(docsBtn, BorderLayout.WEST);
        footerPanel.add(actionButtons, BorderLayout.EAST);

        add(footerPanel, BorderLayout.SOUTH);

        setSize(800, 400);
        setLocationRelativeTo(owner);

        // Cleanup listener on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                conditionEditorViewModel.removePropertyChangeListener(ConditionEditorDialog.this);
            }
        });
    }

    private void showDocumentation() {
        JEditorPane docPane = new JEditorPane();
        docPane.setEditable(false);
        docPane.setContentType("text/html");

        // 1. Load the file from the classpath resources
        java.net.URL helpUrl = getClass().getResource("/undefined_language_documentation.html");

        if (helpUrl != null) {
            try {
                // This loads the HTML file directly
                docPane.setPage(helpUrl);
            } catch (java.io.IOException e) {
                docPane.setText("<html><body style='color:white'>Error loading documentation: " + e.getMessage()
                        + "</body></html>");
            }
        } else {
            docPane.setText("<html><body style='color:white'>Documentation file " +
                    "'undefined_language_documentation.html' not found.</body></html>");
        }

        // 2. Set Caret to top
        docPane.setCaretPosition(0);

        // 3. Wrap in ScrollPane
        JScrollPane scrollPane = new JScrollPane(docPane);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 90)));

        // 4. Show Dialog
        JOptionPane.showMessageDialog(this,
                scrollPane,
                "Undefined Language Specification",
                JOptionPane.PLAIN_MESSAGE);
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
            // NEW: Trigger the callback whenever state changes
            if (onChangeCallback != null) {
                onChangeCallback.run();
            }
            dispose(); // Close window only on success
        }
    }
}
