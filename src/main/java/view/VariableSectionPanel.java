package view;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import view.util.PropertyPanelUtility;
import interface_adapter.variable.VariableViewModel;
import interface_adapter.variable.VariableState;
import interface_adapter.variable.UpdateVariableController;
import interface_adapter.variable.DeleteVariableController;
import use_case.variable.factory.DefaultVariableFactoryRegistry;

import use_case.variable.factory.VariableFactory;
import use_case.variable.factory.ValuePromptKind;


public class VariableSectionPanel extends JPanel implements PropertyChangeListener {

    // One panel per type, created dynamically from the registry
    private final Map<String, JPanel> typePanels = new HashMap<>();

    private VariableViewModel variableViewModel;
    private UpdateVariableController updateVariableController;
    private DeleteVariableController deleteVariableController;

    // Track row panels by their variable identity (name + type + scope)
    private final Map<String, JPanel> variableRowMap = new HashMap<>();

    // Factory registry so we can discover types
    private final DefaultVariableFactoryRegistry factoryRegistry;

    public VariableSectionPanel() {
        this.factoryRegistry = new DefaultVariableFactoryRegistry();

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel contentPanel = createVariableSection();
        add(contentPanel, BorderLayout.CENTER);
    }


    private JPanel createVariableSection() {
        JPanel panel = PropertyPanelUtility.createSectionPanel("Variable");
        GridBagConstraints gbc = PropertyPanelUtility.baseGbc();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        List<String> registeredTypes = factoryRegistry.getRegisteredTypes();

        for (String typeName : registeredTypes) {
            // ----- header -----
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setOpaque(false);

            String headerText = typeName + " variables:";
            JLabel header = PropertyPanelUtility.createSubHeaderLabel(headerText);
            headerPanel.add(header, BorderLayout.WEST);

            // Add button for this type
            JButton addBtn = PropertyPanelUtility.createAddButton();
            addBtn.addActionListener(e -> addVariableOfType(typeName));
            headerPanel.add(addBtn, BorderLayout.EAST);

            panel.add(headerPanel, gbc);
            gbc.gridy++;

            // ----- list panel for this type -----
            JPanel varsPanel = new JPanel();
            varsPanel.setOpaque(false);
            varsPanel.setLayout(new BoxLayout(varsPanel, BoxLayout.Y_AXIS));
            panel.add(varsPanel, gbc);

            // remember which panel corresponds to which type
            typePanels.put(typeName, varsPanel);

            gbc.gridy++;
            panel.add(Box.createVerticalStrut(12), gbc);
            gbc.gridy++;
        }

        return panel;
    }


    private void addVariableOfType(String typeName) {
        String name = JOptionPane.showInputDialog(
                this,
                typeName + " variable name:",
                "Add " + typeName + " Variable",
                JOptionPane.PLAIN_MESSAGE
        );
        if (name == null || name.isBlank()) return;

        if (!isValidVariableName(name)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Variable name must contain only English letters, digits, or underscores.\n" +
                            "Pattern: [A-Za-z0-9_]+",
                    "Invalid Name",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Ask the registry for the factory for this type
        VariableFactory factory = factoryRegistry.get(typeName);
        if (factory == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No factory registered for type: " + typeName,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String valueStr;

        // Use the factory's hint to decide how to ask
        if (factory.getPromptKind() == ValuePromptKind.BOOLEAN) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Initial value true?",
                    "Boolean Value",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );
            if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                return;
            }
            boolean value = (choice == JOptionPane.YES_OPTION);
            valueStr = Boolean.toString(value);
        } else {
            valueStr = JOptionPane.showInputDialog(
                    this,
                    "Initial value:",
                    ""
            );
            if (valueStr == null || valueStr.isBlank()) return;
        }

        if (updateVariableController != null) {
            updateVariableController.updateVariable(name, typeName, false, valueStr);
        }
    }


    private void addVariableRow(String name, String valueStr, String type, boolean isGlobal) {
        JPanel varsPanel = typePanels.get(type);
        if (varsPanel == null) {
            // type unknown in UI; nothing to render
            return;
        }

        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(true);
        row.setBackground(new Color(45, 45, 45));
        row.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        String displayText = (isGlobal ? "[G] " : "") + name + " = " + valueStr;
        JTextField display = new JTextField(displayText);
        display.setEditable(false);
        display.setBackground(new Color(60, 60, 60));
        display.setForeground(Color.WHITE);
        display.setCaretColor(Color.WHITE);
        display.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 90)));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 0));

        JButton editBtn = PropertyPanelUtility.createEditButton();
        editBtn.addActionListener(e -> editVariable(name, valueStr, type, isGlobal));

        JButton deleteBtn = PropertyPanelUtility.createDeleteButton();
        deleteBtn.addActionListener(e -> deleteVariable(name, type, isGlobal));

        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

        row.add(display, BorderLayout.CENTER);
        row.add(buttonPanel, BorderLayout.EAST);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));

        varsPanel.add(row);

        String key = makeKey(name, type, isGlobal);
        variableRowMap.put(key, row);
    }


    private void editVariable(String name, String oldValue, String type, boolean isGlobal) {
        VariableFactory factory = factoryRegistry.get(type);
        if (factory == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No factory registered for type: " + type,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String newValue;

        if (factory.getPromptKind() == ValuePromptKind.BOOLEAN) {
            boolean current = Boolean.parseBoolean(oldValue);

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Set value of " + name + " (" + type + ")?\n"
                            + "Current: " + current + "\n"
                            + "Click Yes for true, No for false.",
                    "Edit Boolean Variable",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );
            if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                return;
            }
            boolean value = (choice == JOptionPane.YES_OPTION);
            newValue = Boolean.toString(value);
        } else {
            newValue = JOptionPane.showInputDialog(
                    this,
                    "New value for " + name + " (" + type + "):",
                    oldValue
            );
            if (newValue == null || newValue.isBlank()) return;
        }

        if (updateVariableController != null) {
            updateVariableController.updateVariable(name, type, isGlobal, newValue);
        }
    }


    private void deleteVariable(String name, String type, boolean isGlobal) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete this variable?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION && deleteVariableController != null) {
            deleteVariableController.deleteVariable(name, type, isGlobal);
        }
    }


    private void syncFromViewModel() {
        if (variableViewModel == null) return;

        VariableState state = variableViewModel.getState();

        // Show error message if present
        if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    state.getErrorMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        // Clear existing UI for all types
        for (JPanel panel : typePanels.values()) {
            panel.removeAll();
        }
        variableRowMap.clear();

        // Rebuild UI from state
        for (VariableState.VariableRow row : state.getVariables()) {
            addVariableRow(row.getName(), row.getValue(), row.getType(), row.isGlobal());
        }

        // Revalidate all type panels
        for (JPanel panel : typePanels.values()) {
            panel.revalidate();
            panel.repaint();
        }
    }

    private String makeKey(String name, String type, boolean isGlobal) {
        return name + "|" + type + "|" + isGlobal;
    }

    public void setVariableViewModel(VariableViewModel viewModel) {
        // Remove old listener if any
        if (this.variableViewModel != null) {
            this.variableViewModel.removePropertyChangeListener(this);
        }

        this.variableViewModel = viewModel;

        // Add new listener
        if (viewModel != null) {
            viewModel.addPropertyChangeListener(this);
            // Initial sync
            syncFromViewModel();
        }
    }

    public void setVariableController(UpdateVariableController controller) {
        this.updateVariableController = controller;
    }

    public void setDeleteVariableController(DeleteVariableController controller) {
        this.deleteVariableController = controller;
    }

    private boolean isValidVariableName(String name) {
        return name != null && name.matches("^[A-Za-z0-9_]+$");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // When ViewModel state changes, sync UI
        if ("state".equals(evt.getPropertyName())) {
            SwingUtilities.invokeLater(this::syncFromViewModel);
        }
    }
}
