package view.property.trigger;

import app.use_case_factory.TriggerUseCaseFactory;
import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import interface_adapter.trigger.create.TriggerCreateController;
import view.util.PropertyPanelUtility;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TriggerManagerPanel extends JPanel implements PropertyChangeListener {

    private final TriggerManagerViewModel viewModel;
    private final TriggerUseCaseFactory triggerUseCaseFactory;
    private final TriggerCreateController triggerCreateController;

    private final JPanel triggerListPanel;

    // Track if we're currently refreshing to prevent recursive updates
    private boolean isRefreshing = false;

    // SINGLETON PATTERN - ensures only one ViewModel exists across all instances
    private static TriggerManagerViewModel singletonViewModel = null;
    private static TriggerUseCaseFactory singletonFactory = null;

    // Synchronization lock to prevent race conditions
    private static final Object LOCK = new Object();

    public TriggerManagerPanel() {
        // Initialize singleton if needed (thread-safe)
        synchronized (LOCK) {
            if (singletonViewModel == null) {
                singletonViewModel = new TriggerManagerViewModel();
                singletonFactory = new TriggerUseCaseFactory(singletonViewModel);
            } else {
            }
        }

        this.viewModel = singletonViewModel;
        this.triggerUseCaseFactory = singletonFactory;
        this.triggerCreateController = triggerUseCaseFactory.createTriggerCreateController();

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel mainSection = PropertyPanelUtility.createSectionPanel("Trigger Manager");

        // Create and Add the "Add (+)" Button
        JButton addButton = PropertyPanelUtility.createAddButton();
        addButton.addActionListener(e -> {
            triggerCreateController.execute();
        });

        GridBagConstraints btnGbc = PropertyPanelUtility.baseGbc();
        btnGbc.gridx = 0;
        btnGbc.gridy = 0;
        btnGbc.anchor = GridBagConstraints.NORTHEAST;
        btnGbc.fill = GridBagConstraints.NONE;
        btnGbc.weightx = 1.0;
        btnGbc.weighty = 0.0;
        mainSection.add(addButton, btnGbc);

        // Create the Trigger List Container
        triggerListPanel = new JPanel();
        triggerListPanel.setLayout(new BoxLayout(triggerListPanel, BoxLayout.Y_AXIS));
        triggerListPanel.setOpaque(false);

        // Add list below the button
        GridBagConstraints listGbc = PropertyPanelUtility.baseGbc();
        listGbc.gridx = 0;
        listGbc.gridy = 1;
        listGbc.weighty = 1.0;
        listGbc.fill = GridBagConstraints.BOTH;
        mainSection.add(triggerListPanel, listGbc);

        add(mainSection, BorderLayout.CENTER);

        // Setup Listeners
        this.viewModel.addPropertyChangeListener(this);
        refresh();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Prevent recursive updates
        if (isRefreshing) {
            return;
        }

        // Use invokeLater to defer the refresh until after the current event completes
        SwingUtilities.invokeLater(() -> {
            refresh();
        });
    }

    private void refresh() {
        if (isRefreshing) {
            return;
        }

        try {
            isRefreshing = true;

            triggerListPanel.removeAll();

            TriggerManagerState state = viewModel.getState();
            int triggerCount = state.getTriggerCount();
            for (int i = 0; i < triggerCount; i++) {
                TriggerPanel panel = new TriggerPanel(i, viewModel, triggerUseCaseFactory);

                // Prevent horizontal stretching issues in BoxLayout
                panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));

                triggerListPanel.add(panel);
                triggerListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }

            revalidate();
            repaint();
        } finally {
            isRefreshing = false;
        }
    }
}