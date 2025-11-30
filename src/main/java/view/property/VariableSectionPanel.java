package view.property;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import view.util.PropertyPanelUtility;
import interface_adapter.variable.LocalVariableViewModel;
import interface_adapter.variable.GlobalVariableViewModel;
import interface_adapter.variable.VariableState;
import interface_adapter.variable.UpdateVariableController;
import interface_adapter.variable.DeleteVariableController;
import use_case.variable.factory.DefaultVariableFactoryRegistry;

import use_case.variable.factory.VariableFactory;
import use_case.variable.factory.ValuePromptKind;


public class VariableSectionPanel extends JPanel implements PropertyChangeListener {

    // Separate panel maps for local and global variables
    private final Map<String, JPanel> localTypePanels = new HashMap<>();
    private final Map<String, JPanel> globalTypePanels = new HashMap<>();

    // Separate ViewModels for local and global
    private LocalVariableViewModel localVariableViewModel;
    private GlobalVariableViewModel globalVariableViewModel;

    private UpdateVariableController updateVariableController;
    private DeleteVariableController deleteVariableController;

    // Track row panels by their variable identity (name + type + scope)
    private final Map<String, JPanel> variableRowMap = new HashMap<>();

    // Factory registry so we can discover types
    private final DefaultVariableFactoryRegistry factoryRegistry;

    // Callback for Auto-Save
    private Runnable onChangeCallback;

    public VariableSectionPanel() {
        this.factoryRegistry = new DefaultVariableFactoryRegistry();

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        JPanel globalContentPanel = createGlobalVariableSection();
        container.add(globalContentPanel);

        container.add(Box.createVerticalStrut(20));

        JPanel localContentPanel = createLocalVariableSection();
        container.add(localContentPanel);

        add(container, BorderLayout.CENTER);
    }

    public void setOnChangeCallback(Runnable callback) {
        this.onChangeCallback = callback;
    }

    private JPanel createLocalVariableSection() {
        JPanel panel = PropertyPanelUtility.createSectionPanel("Local Variables");
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

            // Add button for LOCAL variables
            JButton addBtn = PropertyPanelUtility.createAddButton();
            addBtn.addActionListener(e -> addVariableOfType(typeName, false)); // false = local
            headerPanel.add(addBtn, BorderLayout.EAST);

            panel.add(headerPanel, gbc);
            gbc.gridy++;

            // ----- list panel for this type -----
            JPanel varsPanel = new JPanel();
            varsPanel.setOpaque(false);
            varsPanel.setLayout(new BoxLayout(varsPanel, BoxLayout.Y_AXIS));
            panel.add(varsPanel, gbc);

            // Store in LOCAL panel map
            localTypePanels.put(typeName, varsPanel);

            gbc.gridy++;
            panel.add(Box.createVerticalStrut(12), gbc);
            gbc.gridy++;
        }

        return panel;
    }

    private JPanel createGlobalVariableSection() {
        JPanel panel = PropertyPanelUtility.createSectionPanel("Global Variables");
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

            JButton addBtn = PropertyPanelUtility.createAddButton();
            addBtn.addActionListener(e -> addVariableOfType(typeName, true)); // true = global
            headerPanel.add(addBtn, BorderLayout.EAST);

            panel.add(headerPanel, gbc);
            gbc.gridy++;

            // ----- list panel for this type -----
            JPanel varsPanel = new JPanel();
            varsPanel.setOpaque(false);
            varsPanel.setLayout(new BoxLayout(varsPanel, BoxLayout.Y_AXIS));
            panel.add(varsPanel, gbc);

            globalTypePanels.put(typeName, varsPanel);

            gbc.gridy++;
            panel.add(Box.createVerticalStrut(12), gbc);
            gbc.gridy++;
        }

        return panel;
    }


    private void addVariableOfType(String typeName, boolean isGlobal) {
        String scopeLabel = isGlobal ? "Global" : "Local";

        String name = JOptionPane.showInputDialog(
                this,
                typeName + " variable name:",
                "Add " + scopeLabel + " " + typeName + " Variable",
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
            updateVariableController.updateVariable(name, typeName, isGlobal, valueStr);
        }
    }


    private void addVariableRow(String name, String valueStr, String type, boolean isGlobal) {
        // Choose the correct panel map based on scope
        Map<String, JPanel> targetPanelMap = isGlobal ? globalTypePanels : localTypePanels;

        JPanel varsPanel = targetPanelMap.get(type);
        if (varsPanel == null) {
            // type unknown in UI; nothing to render
            return;
        }

        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(true);
        row.setBackground(new Color(45, 45, 45));
        row.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        String scopePrefix = isGlobal ? "[G] " : "[L] ";
        String displayText = scopePrefix + name + " = " + valueStr;
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


    private void syncLocalFromViewModel() {
        if (localVariableViewModel == null) return;

        VariableState state = localVariableViewModel.getState();

        // Show error message if present
        if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    state.getErrorMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        // Clear existing UI for local only
        for (JPanel panel : localTypePanels.values()) {
            panel.removeAll();
        }

        // Remove local variable rows from map
        variableRowMap.entrySet().removeIf(entry -> entry.getKey().endsWith("|false"));

        // Rebuild local UI from state
        for (VariableState.VariableRow row : state.getVariables()) {
            if (!row.isGlobal()) {  // Only add local variables
                addVariableRow(row.getName(), row.getValue(), row.getType(), row.isGlobal());
            }
        }

        // Revalidate local panels
        for (JPanel panel : localTypePanels.values()) {
            panel.revalidate();
            panel.repaint();
        }
    }

    private void syncGlobalFromViewModel() {
        if (globalVariableViewModel == null) return;

        VariableState state = globalVariableViewModel.getState();

        // Show error message if present
        if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    state.getErrorMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        // Clear existing UI for global only
        for (JPanel panel : globalTypePanels.values()) {
            panel.removeAll();
        }

        // Remove global variable rows from map
        variableRowMap.entrySet().removeIf(entry -> entry.getKey().endsWith("|true"));

        // Rebuild global UI from state
        for (VariableState.VariableRow row : state.getVariables()) {
            if (row.isGlobal()) {  // Only add global variables
                addVariableRow(row.getName(), row.getValue(), row.getType(), row.isGlobal());
            }
        }

        // Revalidate global panels
        for (JPanel panel : globalTypePanels.values()) {
            panel.revalidate();
            panel.repaint();
        }
    }

    private String makeKey(String name, String type, boolean isGlobal) {
        return name + "|" + type + "|" + isGlobal;
    }

    public void setLocalVariableViewModel(LocalVariableViewModel viewModel) {
        // Remove old listener if any
        if (this.localVariableViewModel != null) {
            this.localVariableViewModel.removePropertyChangeListener(this);
        }

        this.localVariableViewModel = viewModel;

        // Add new listener
        if (viewModel != null) {
            viewModel.addPropertyChangeListener(this);
            // Initial sync
            syncLocalFromViewModel();
        }
    }

    public void setGlobalVariableViewModel(GlobalVariableViewModel viewModel) {
        // Remove old listener if any
        if (this.globalVariableViewModel != null) {
            this.globalVariableViewModel.removePropertyChangeListener(this);
        }

        this.globalVariableViewModel = viewModel;

        // Add new listener
        if (viewModel != null) {
            viewModel.addPropertyChangeListener(this);
            // Initial sync
            syncGlobalFromViewModel();
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
            Object source = evt.getSource();

            // Determine which ViewModel triggered the change
            if (source == localVariableViewModel) {
                SwingUtilities.invokeLater(this::syncLocalFromViewModel);
            } else if (source == globalVariableViewModel) {
                SwingUtilities.invokeLater(this::syncGlobalFromViewModel);
            }

            // Trigger Auto-Save!
            if (onChangeCallback != null) {
                onChangeCallback.run();
            }
        }
    }
}