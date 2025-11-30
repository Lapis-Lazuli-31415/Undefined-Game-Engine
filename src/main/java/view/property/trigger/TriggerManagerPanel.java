package view.property.trigger;

import app.use_case_factory.TriggerUseCaseFactory;
import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import interface_adapter.trigger.action.ActionEditorViewModel;
import interface_adapter.trigger.condition.ConditionEditorViewModel;
import interface_adapter.trigger.create.TriggerCreateController;
import view.util.PropertyPanelUtility;

// --- Imports for Entity conversion ---
import entity.scripting.Trigger;
import entity.scripting.condition.Condition;
import entity.scripting.action.Action;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
// -------------------------------------

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

    private Runnable onChangeCallback;

    public TriggerManagerPanel() {
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

    public void setOnChangeCallback(Runnable callback) {
        this.onChangeCallback = callback;
    }

    // load data from the Entity to the UI
    public void loadTriggerManager(entity.scripting.TriggerManager manager) {
        // 1. Reset the View Model to a clean state
        triggerManagerViewModel.setState(new TriggerManagerState());
        TriggerManagerState state = triggerManagerViewModel.getState();

        if (manager != null) {
            // 2. Loop through loaded Entities
            for (Trigger t : manager.getTriggers()) {
                String event = t.getEvent().getEventLabel();
                Map<String, String> params = t.getEvent().getEventParameters();

                // Convert Conditions to Scripts
                List<String> conditions = new ArrayList<>();
                for (Condition c : t.getConditions()) {
                    conditions.add(c.format());
                }

                // Convert Actions to Scripts
                List<String> actions = new ArrayList<>();
                for (Action a : t.getActions()) {
                    actions.add(a.format());
                }

                // 3. Add to View State
                state.addTrigger(event, params, conditions, actions);
            }
        }

        // 4. Trigger UI Refresh
        triggerManagerViewModel.firePropertyChange();
        refresh();
    }
    // -------------------------------------------------------------

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refresh();
        if (onChangeCallback != null) {
            onChangeCallback.run();
        }
    }

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