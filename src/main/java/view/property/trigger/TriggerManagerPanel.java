package view.property.trigger;

import app.use_case_factory.TriggerUseCaseFactory;
import interface_adapter.trigger.TriggerManagerState;
import interface_adapter.trigger.TriggerManagerViewModel;
import interface_adapter.trigger.create.TriggerCreateController;
import interface_adapter.trigger.delete.TriggerDeleteController;
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

    public TriggerManagerPanel() {

        viewModel = new TriggerManagerViewModel();
        triggerUseCaseFactory = new TriggerUseCaseFactory(viewModel);
        triggerCreateController = triggerUseCaseFactory.createTriggerCreateController();

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel mainSection = PropertyPanelUtility.createSectionPanel("Trigger Manager");

        // 3. Create and Add the "Add (+)" Button
        // We place it at the Top-Right (NorthEast) inside the section
        JButton addButton = PropertyPanelUtility.createAddButton();
        addButton.addActionListener(e -> triggerCreateController.execute());

        GridBagConstraints btnGbc = PropertyPanelUtility.baseGbc();
        btnGbc.gridx = 0;
        btnGbc.gridy = 0;
        btnGbc.anchor = GridBagConstraints.NORTHEAST; // Pin to top-right
        btnGbc.fill = GridBagConstraints.NONE; // Don't stretch the button
        btnGbc.weightx = 1.0;
        btnGbc.weighty = 0.0;
        mainSection.add(addButton, btnGbc);

        // 4. Create the Trigger List Container
        triggerListPanel = new JPanel();
        triggerListPanel.setLayout(new BoxLayout(triggerListPanel, BoxLayout.Y_AXIS));
        triggerListPanel.setOpaque(false);

        // Add list below the button
        GridBagConstraints listGbc = PropertyPanelUtility.baseGbc();
        listGbc.gridx = 0;
        listGbc.gridy = 1;
        listGbc.weighty = 1.0; // Allow list to grow vertically
        listGbc.fill = GridBagConstraints.BOTH; // Fill available space
        mainSection.add(triggerListPanel, listGbc);

        add(mainSection, BorderLayout.CENTER);

        // 5. Setup Listeners
        this.viewModel.addPropertyChangeListener(this);
        refresh();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refresh();
    }

    private void refresh() {
        triggerListPanel.removeAll();

        TriggerManagerState state = viewModel.getState();
        for (int i = 0; i < state.getTriggerCount(); i++) {
            // Direct constructor call as requested
            TriggerPanel panel = new TriggerPanel(i, viewModel, triggerUseCaseFactory);

            // Prevent horizontal stretching issues in BoxLayout
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));

            triggerListPanel.add(panel);
            triggerListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        revalidate();
        repaint();
    }

    // --- Example Integration (Main method to show it works) ---
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame homeView = new JFrame("Properties Panel Mockup");
            homeView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            homeView.setSize(400, 600);
            homeView.setLayout(new BorderLayout());

            // --- Mock Properties Panel (The one that actually scrolls) ---
            JPanel propertiesPanelContent = new JPanel();
            propertiesPanelContent.setLayout(new BoxLayout(propertiesPanelContent, BoxLayout.Y_AXIS));
            propertiesPanelContent.setBackground(new Color(40, 40, 40));

            // Add some mock content above
            propertiesPanelContent.add(new JLabel("<html><font color='white'>General Object Settings...</font></html>"));
            propertiesPanelContent.add(Box.createVerticalStrut(20));

            // Add the Trigger Manager (your refactored component)
            TriggerManagerPanel triggerManager = new TriggerManagerPanel();
            triggerManager.setAlignmentX(Component.LEFT_ALIGNMENT); // Critical for BoxLayout stacking
            propertiesPanelContent.add(triggerManager);

            // Add filler content below to force scrolling on the parent
            propertiesPanelContent.add(Box.createVerticalStrut(30));
            propertiesPanelContent.add(new JLabel("<html><font color='white'>More data below...</font></html>"));
            propertiesPanelContent.add(Box.createVerticalStrut(800)); // Forces scrollbar to appear

            // The scroll pane now wraps the Properties Panel content
            JScrollPane scrollPane = new JScrollPane(propertiesPanelContent);
            scrollPane.getViewport().setBackground(new Color(40, 40, 40));
            scrollPane.setBorder(BorderFactory.createEmptyBorder());

            homeView.add(scrollPane, BorderLayout.CENTER);
            homeView.setVisible(true);
        });
    }
}