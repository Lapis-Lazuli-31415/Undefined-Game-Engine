package view.property.trigger;

import app.use_case_factory.TriggerUseCaseFactory;
import entity.scripting.event.DefaultEventFactory;
import entity.scripting.event.Event;
import entity.scripting.event.EventFactory;
import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import interface_adapter.trigger.event.change.EventChangeController;
import interface_adapter.trigger.event.parameter_change.EventParameterChangeController;
import view.util.PropertyPanelUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

public class EventPanel extends JPanel {

    private final EventFactory eventFactory;
    private final EventChangeController eventChangeController;
    private final EventParameterChangeController eventParameterChangeController;

    public EventPanel(int triggerIndex,
                      TriggerManagerViewModel viewModel,
                      TriggerUseCaseFactory triggerUseCaseFactory) {

        eventFactory = triggerUseCaseFactory.getEventFactory();
        eventChangeController = triggerUseCaseFactory.createEventChangeController();
        eventParameterChangeController = triggerUseCaseFactory.createEventParameterChangeController();

        TriggerManagerState state = viewModel.getState();

        // 1. Setup Section Style
        setLayout(new GridBagLayout());
        setOpaque(false);
        setBorder(PropertyPanelUtility.makeSectionBorder("Event"));

        // 2. Event Selection Row
        // Label
        JLabel typeLabel = PropertyPanelUtility.createInlineLabel("Type:");
        GridBagConstraints gbc = PropertyPanelUtility.baseGbc();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0; // Don't expand label
        gbc.fill = GridBagConstraints.NONE;
        add(typeLabel, gbc);

        // ComboBox
        String[] registeredEvents = eventFactory.getRegisteredEvents().toArray(new String[0]);
        JComboBox<String> eventBox = new JComboBox<>(registeredEvents);
        PropertyPanelUtility.styleCombo(eventBox);

        // Get current event name from state
        String currentEventName = state.getTriggerEvent(triggerIndex);
        eventBox.setSelectedItem(currentEventName);

        // Listener for Event Change
        eventBox.addActionListener(e ->
                eventChangeController.execute(triggerIndex, (String) eventBox.getSelectedItem())
        );

        gbc.gridx = 1;
        gbc.weightx = 1.0; // Expand combo box
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(eventBox, gbc);

        // 3. Dynamic Parameters Section
        // We need an instance of the event to know its required parameters.
        // Assuming factory.createEvent(name) returns a prototype instance.
        Event eventPrototype = eventFactory.create(currentEventName);

        if (eventPrototype != null) {
            List<String> requiredParams = eventPrototype.getRequiredParameters();

            int row = 1;
            for (String paramName : requiredParams) {
                // Parameter Label
                JLabel paramLabel = PropertyPanelUtility.createInlineLabel(paramName + ":");
                gbc.gridx = 0;
                gbc.gridy = row;
                gbc.weightx = 0;
                gbc.fill = GridBagConstraints.NONE;
                gbc.insets = new Insets(5, 2, 2, 2); // Slightly more top padding for params
                add(paramLabel, gbc);

                // Parameter Input Field
                // Get current value from state (assuming state provides this access)
                String currentValue = state.getTriggerEventParameter(triggerIndex, paramName);
                JTextField paramField = PropertyPanelUtility.smallField(currentValue);

                // Add Listener to update parameter on change
                addParameterListener(paramField, triggerIndex, paramName, triggerUseCaseFactory);

                gbc.gridx = 1;
                gbc.weightx = 1.0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                add(paramField, gbc);

                row++;
            }
        }
    }

    /**
     * Attaches a listener to the text field to trigger the ParameterChangeController.
     */
    private void addParameterListener(JTextField field, int index, String paramName, TriggerUseCaseFactory factory) {
        // 1. Create the update logic
        Runnable updateAction = () -> {
            // Only execute if the value actually changed (optional optimization)
            eventParameterChangeController.execute(index, paramName, field.getText());
        };

        // 2. Update when the user clicks away (loses focus)
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateAction.run();
            }
        });

        // 3. Update when the user presses "Enter"
        field.addActionListener(e -> {
            updateAction.run();
            // Optional: Transfer focus to the next component so the UI refresh feels natural
            field.transferFocus();
        });
    }
}
