package view.property.trigger;

import app.use_case_factory.TriggerUseCaseFactory;
import entity.scripting.Trigger;
import entity.scripting.action.ActionFactory;
import entity.scripting.action.DefaultActionFactory;
import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import interface_adapter.trigger.action.ActionEditorViewModel;
import interface_adapter.trigger.action.change.ActionChangeController;
import interface_adapter.trigger.action.create.ActionCreateController;
import interface_adapter.trigger.action.delete.ActionDeleteController;
import interface_adapter.trigger.action.edit.ActionEditController;
import view.util.PropertyPanelUtility;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ActionListPanel extends JPanel {

    // Assuming ActionFactory exists similar to ConditionFactory/EventFactory
    private final ActionFactory actionFactory;
    private final ActionCreateController actionCreateController;
    private final ActionDeleteController actionDeleteController;
    private final ActionChangeController actionChangeController;
    private final ActionEditController actionEditController;

    public ActionListPanel(int triggerIndex,
                           TriggerManagerViewModel viewModel,
                           ActionEditorViewModel actionEditorViewModel,
                           TriggerUseCaseFactory triggerUseCaseFactory,
                           Runnable onChangeCallback) {

        // Initialize triggerUseCaseFactory (or pass it in if available in your architecture)
        actionFactory = triggerUseCaseFactory.getActionFactory();
        actionCreateController = triggerUseCaseFactory.createActionCreateController();
        actionDeleteController = triggerUseCaseFactory.createActionDeleteController();
        actionChangeController = triggerUseCaseFactory.createActionChangeController();
        actionEditController = triggerUseCaseFactory.createActionEditController();

        TriggerManagerState state = viewModel.getState();
        List<String> actions = state.getTriggerActions(triggerIndex);

        // 1. Setup Main Section Style
        setLayout(new GridBagLayout());
        setOpaque(false);
        setBorder(PropertyPanelUtility.makeSectionBorder("Actions"));

        // 2. Add Button (Top Right Header)
        JButton addBtn = PropertyPanelUtility.createAddButton();
        addBtn.addActionListener(e ->
                actionCreateController.execute(triggerIndex)
        );

        GridBagConstraints gbc = PropertyPanelUtility.baseGbc();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        add(addBtn, gbc);

        // 3. Action List Container
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setOpaque(false);

        // Populate the list
        for (int i = 0; i < actions.size(); i++) {
            String actionType = actions.get(i);
            JPanel row = createActionRow(triggerIndex, i, actionType, actionEditorViewModel,
                    triggerUseCaseFactory, onChangeCallback);

            listContainer.add(row);
            listContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        // Add List Container to Main Panel (Row 1)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(listContainer, gbc);
    }

    /**
     * Creates a single row for an action: [Dropdown] [Edit Button] [Delete Button]
     */
    private JPanel createActionRow(int triggerIndex, int actionIndex, String currentType,
                                   ActionEditorViewModel viewModel, TriggerUseCaseFactory factory,
                                   Runnable onChangeCallback) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);

        GridBagConstraints rowGbc = new GridBagConstraints();
        rowGbc.fill = GridBagConstraints.HORIZONTAL;
        rowGbc.insets = new Insets(0, 0, 0, 5); // Spacing between elements

        // A. Action Type Dropdown
        String[] availableTypes = actionFactory.getRegisteredActions().toArray(new String[0]);
        JComboBox<String> typeBox = new JComboBox<>(availableTypes);
        PropertyPanelUtility.styleCombo(typeBox);
        typeBox.setSelectedItem(currentType);

        typeBox.addActionListener(e ->
                actionChangeController.execute(
                        triggerIndex,
                        actionIndex,
                        (String) typeBox.getSelectedItem()
                )
        );

        rowGbc.gridx = 0;
        rowGbc.weightx = 1.0; // Dropdown takes available width
        row.add(typeBox, rowGbc);

        // B. Edit Button (Uses the small utility method with pencil icon)
        JButton editBtn = PropertyPanelUtility.createEditButton();
        // Add listener here when you implement ActionParameterController
        editBtn.addActionListener(e -> {
            // 1. Trigger Controller to load script into ViewModel
            actionEditController.execute(triggerIndex, actionIndex);

            // 2. Get script from ViewModel (assuming Controller updated it synchronously or State has it)
            String script = viewModel.getState().getAction();

            // 3. Open Dialog
            ActionEditorDialog dialog = new ActionEditorDialog(
                    SwingUtilities.getWindowAncestor(this),
                    triggerIndex,
                    actionIndex,
                    script,
                    viewModel,
                    factory,
                    onChangeCallback
            );
            dialog.setVisible(true);
        });

        rowGbc.gridx = 1;
        rowGbc.weightx = 0; // Fixed width
        row.add(editBtn, rowGbc);

        // C. Delete Button (Uses the small utility method with X icon)
        JButton delBtn = PropertyPanelUtility.createDeleteButton();

        delBtn.addActionListener(e ->
                actionDeleteController.execute(triggerIndex, actionIndex)
        );

        rowGbc.gridx = 2;
        row.add(delBtn, rowGbc);

        return row;
    }
}