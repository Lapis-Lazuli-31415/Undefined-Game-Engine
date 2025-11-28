package view.property.trigger;

import app.use_case_factory.TriggerUseCaseFactory;
import entity.scripting.condition.ConditionFactory;
import entity.scripting.condition.DefaultConditionFactory;
import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import interface_adapter.trigger.condition.change.ConditionChangeController;
import interface_adapter.trigger.condition.create.ConditionCreateController;
import interface_adapter.trigger.condition.delete.ConditionDeleteController;
import view.util.PropertyPanelUtility;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ConditionListPanel extends JPanel {

    private final ConditionFactory conditionFactory;
    private final ConditionCreateController conditionCreateController;
    private final ConditionDeleteController conditionDeleteController;
    private final ConditionChangeController conditionChangeController;

    public ConditionListPanel(int triggerIndex,
                              TriggerManagerViewModel viewModel,
                              TriggerUseCaseFactory triggerUseCaseFactory) {

        // Initialize triggerUseCaseFactory
        this.conditionFactory = new DefaultConditionFactory();
        conditionCreateController = triggerUseCaseFactory.createConditionCreateController();
        conditionDeleteController = triggerUseCaseFactory.createConditionDeleteController();
        conditionChangeController = triggerUseCaseFactory.createConditionChangeController();

        TriggerManagerState state = viewModel.getState();
        List<String> conditions = state.getTriggerConditions(triggerIndex);

        // 1. Setup Main Section Style
        setLayout(new GridBagLayout());
        setOpaque(false);
        setBorder(PropertyPanelUtility.makeSectionBorder("Conditions"));

        // 2. Add Button (Top Right)
        JButton addBtn = PropertyPanelUtility.createAddButton();
        addBtn.addActionListener(e ->
                conditionCreateController.execute(triggerIndex)
        );

        GridBagConstraints gbc = PropertyPanelUtility.baseGbc();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        add(addBtn, gbc);

        // 3. Condition List Container
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setOpaque(false);

        // Populate the list
        for (int i = 0; i < conditions.size(); i++) {
            String conditionType = conditions.get(i);
            JPanel row = createConditionRow(triggerIndex, i, conditionType, triggerUseCaseFactory);

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

    private JPanel createConditionRow(int triggerIndex, int conditionIndex, String currentType, TriggerUseCaseFactory factory) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);

        GridBagConstraints rowGbc = new GridBagConstraints();
        rowGbc.fill = GridBagConstraints.HORIZONTAL;
        rowGbc.insets = new Insets(0, 0, 0, 5); // Spacing between elements

        // A. Condition Type Dropdown
        String[] availableTypes = conditionFactory.getRegisteredConditions().toArray(new String[0]);
        JComboBox<String> typeBox = new JComboBox<>(availableTypes);
        PropertyPanelUtility.styleCombo(typeBox);
        typeBox.setSelectedItem(currentType);

        typeBox.addActionListener(e ->
                conditionChangeController.execute(triggerIndex, conditionIndex, (String) typeBox.getSelectedItem())
        );

        rowGbc.gridx = 0;
        rowGbc.weightx = 1.0;
        row.add(typeBox, rowGbc);

        // B. Edit Button
        JButton editBtn = PropertyPanelUtility.createEditButton();

        rowGbc.gridx = 1;
        rowGbc.weightx = 0;
        row.add(editBtn, rowGbc);

        // C. Delete Button
        // Use a simple text minus or "X".
        // Note: Using "X" is often clearer if no specific icon is available.
        JButton delBtn = PropertyPanelUtility.createDeleteButton();

        delBtn.addActionListener(e ->
                conditionDeleteController.execute(triggerIndex, conditionIndex)
        );

        rowGbc.gridx = 2;
        row.add(delBtn, rowGbc);

        return row;
    }
}
