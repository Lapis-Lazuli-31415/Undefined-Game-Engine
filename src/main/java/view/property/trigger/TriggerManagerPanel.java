package view.property.trigger;

import app.use_case_factory.TriggerUseCaseFactory;
import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import interface_adapter.trigger.action.ActionEditorViewModel;
import interface_adapter.trigger.condition.ConditionEditorViewModel;
import interface_adapter.trigger.create.TriggerCreateController;
import view.util.PropertyPanelUtility;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TriggerManagerPanel extends JPanel implements PropertyChangeListener {

    private final TriggerManagerViewModel triggerManagerViewModel;
    private final ConditionEditorViewModel conditionEditorViewModel;
    private final ActionEditorViewModel actionEditorViewModel;
    private final TriggerUseCaseFactory triggerUseCaseFactory;
    private final TriggerCreateController triggerCreateController;

    private final JPanel triggerListPanel;

    // NEW: Callback for auto-save
    private Runnable onChangeCallback;

    public TriggerManagerPanel() {
        // ... (Keep existing constructor logic exactly as is until the listeners setup) ...
        triggerManagerViewModel = new TriggerManagerViewModel();
        conditionEditorViewModel = new ConditionEditorViewModel();
        actionEditorViewModel = new ActionEditorViewModel();
        triggerUseCaseFactory = new TriggerUseCaseFactory(triggerManagerViewModel,
                conditionEditorViewModel, actionEditorViewModel);
        triggerCreateController = triggerUseCaseFactory.createTriggerCreateController();

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel mainSection = PropertyPanelUtility.createSectionPanel("Trigger Manager");

        JButton addButton = PropertyPanelUtility.createAddButton();
        addButton.addActionListener(e -> triggerCreateController.execute());

        GridBagConstraints btnGbc = PropertyPanelUtility.baseGbc();
        btnGbc.gridx = 0;
        btnGbc.gridy = 0;
        btnGbc.anchor = GridBagConstraints.NORTHEAST;
        btnGbc.fill = GridBagConstraints.NONE;
        btnGbc.weightx = 1.0;
        btnGbc.weighty = 0.0;
        mainSection.add(addButton, btnGbc);

        triggerListPanel = new JPanel();
        triggerListPanel.setLayout(new BoxLayout(triggerListPanel, BoxLayout.Y_AXIS));
        triggerListPanel.setOpaque(false);

        GridBagConstraints listGbc = PropertyPanelUtility.baseGbc();
        listGbc.gridx = 0;
        listGbc.gridy = 1;
        listGbc.weighty = 1.0;
        listGbc.fill = GridBagConstraints.BOTH;
        mainSection.add(triggerListPanel, listGbc);

        add(mainSection, BorderLayout.CENTER);

        this.triggerManagerViewModel.addPropertyChangeListener(this);
        refresh();
    }

    // NEW: Setter for the callback
    public void setOnChangeCallback(Runnable callback) {
        this.onChangeCallback = callback;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refresh();
        // NEW: Trigger the callback whenever state changes
        if (onChangeCallback != null) {
            onChangeCallback.run();
        }
    }

    // ... (Keep the rest of the file: refresh(), methods, etc.) ...
    private void refresh() {
        triggerListPanel.removeAll();

        TriggerManagerState state = triggerManagerViewModel.getState();
        for (int i = 0; i < state.getTriggerCount(); i++) {
            TriggerPanel panel = new TriggerPanel(i, triggerManagerViewModel,
                    conditionEditorViewModel, actionEditorViewModel, triggerUseCaseFactory);
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));

            triggerListPanel.add(panel);
            triggerListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        revalidate();
        repaint();
    }

    public String getSelectedEvent() { return null; }
    public String getSelectedKey() { return null; }
    public java.util.List<String> getConditionTexts() { return java.util.List.of(); }
    public java.util.List<String> getActionTexts() { return java.util.List.of(); }
}