package view.property.trigger;

import app.use_case_factory.TriggerUseCaseFactory;
import interface_adapter.trigger.TriggerManagerViewModel;
import interface_adapter.trigger.delete.TriggerDeleteController;
import view.util.PropertyPanelUtility;

import javax.swing.*;
import java.awt.*;

public class TriggerPanel extends JPanel {

    private final TriggerDeleteController triggerDeleteController;

    public TriggerPanel(
            int triggerIndex,
            TriggerManagerViewModel viewModel,
            TriggerUseCaseFactory factory) {

        triggerDeleteController = factory.createTriggerDeleteController();

        // 1. Apply Main Section Style
        setLayout(new GridBagLayout());
        setOpaque(false);
        setBorder(PropertyPanelUtility.makeSectionBorder("Trigger " + (triggerIndex + 1)));

        // 2. Create Delete Button (Top Right)
        JButton deleteBtn = PropertyPanelUtility.createDeleteButton();
        deleteBtn.addActionListener(e -> triggerDeleteController.execute(triggerIndex));

        // Place Delete Button: Row 0, Top-Right Anchor
        GridBagConstraints btnGbc = PropertyPanelUtility.baseGbc();
        btnGbc.gridx = 0;
        btnGbc.gridy = 0;
        btnGbc.weightx = 1.0; // Push to the right
        btnGbc.anchor = GridBagConstraints.NORTHEAST;
        btnGbc.fill = GridBagConstraints.NONE; // Keep fixed size
        btnGbc.insets = new Insets(0, 0, 5, 4); // Small gap below button
        add(deleteBtn, btnGbc);

        // 3. Event Section
        // Row 1
        GridBagConstraints contentGbc = PropertyPanelUtility.baseGbc();
        contentGbc.gridy = 1;
        contentGbc.insets = new Insets(0, 2, 10, 2); // Spacing between sections
        add(new EventPanel(triggerIndex, viewModel, factory), contentGbc);

        // 4. Conditions Section
        // Row 2
        contentGbc.gridy = 2;
        add(new ConditionListPanel(triggerIndex, viewModel, factory), contentGbc);

        // 5. Actions Section
        // Row 3
        contentGbc.gridy = 3;
        // Use weightY if you want this to expand, otherwise default is fine
        add(new ActionListPanel(triggerIndex, viewModel, factory), contentGbc);
    }
}
