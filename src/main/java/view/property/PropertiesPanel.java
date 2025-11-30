package view.property;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

import interface_adapter.transform.TransformViewModel;
import interface_adapter.transform.TransformController;
import interface_adapter.variable.LocalVariableViewModel;
import interface_adapter.variable.GlobalVariableViewModel;
import interface_adapter.variable.UpdateVariableController;
import interface_adapter.variable.DeleteVariableController;
import view.property.trigger.TriggerManagerPanel;

public class PropertiesPanel extends JPanel {

    // Section panels
    private final TransformSectionPanel transformSection;
    private final SpriteRendererSectionPanel spriteRendererSection;
    private final TriggerManagerPanel triggerManagerPanel;
    private final VariableSectionPanel variableSection;

    public PropertiesPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(45, 45, 45));

        // Outer "Properties" title with white text
        TitledBorder outerBorder = makeSectionBorder("Properties");
        setBorder(BorderFactory.createCompoundBorder(
                outerBorder,
                new EmptyBorder(5, 5, 5, 5)
        ));

        // Initialize section panels
        transformSection = new TransformSectionPanel();
        spriteRendererSection = new SpriteRendererSectionPanel();
        triggerManagerPanel = new TriggerManagerPanel();
        variableSection = new VariableSectionPanel();

        // Add sections
        add(Box.createVerticalStrut(10));
        add(transformSection);
        add(Box.createVerticalStrut(20));
        add(spriteRendererSection);
        add(Box.createVerticalStrut(20));
        add(triggerManagerPanel);
        add(Box.createVerticalStrut(20));
        add(variableSection);
        add(Box.createVerticalGlue());
    }

    public void setAutoSaveCallback(Runnable autoSaveCallback) {
        // Pass callback to Trigger Manager
        triggerManagerPanel.setOnChangeCallback(autoSaveCallback);

        // Pass callback to Variable Section
        variableSection.setOnChangeCallback(autoSaveCallback);

        // Note: Transform callback is passed in the .bind() method separately
    }

    // ---------- helper UI methods ----------

    // border creator with white title text
    private TitledBorder makeSectionBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 11),
                Color.WHITE
        );
    }

    public void bind(TransformViewModel viewModel,
                     TransformController controller,
                     Runnable onChangeCallback) {
        transformSection.bind(viewModel, controller, onChangeCallback);
    }

    public void bindSpriteRenderer(entity.GameObject gameObject,
                                   interface_adapter.assets.AssetLibViewModel assetLibViewModel,
                                   Runnable onChangeCallback) {
        spriteRendererSection.bind(gameObject, assetLibViewModel, onChangeCallback);
    }

    public void setLocalVariableViewModel(LocalVariableViewModel viewModel) {
        variableSection.setLocalVariableViewModel(viewModel);
    }

    public void setGlobalVariableViewModel(GlobalVariableViewModel viewModel) {
        variableSection.setGlobalVariableViewModel(viewModel);
    }

    public void setVariableController(UpdateVariableController controller) {
        variableSection.setVariableController(controller);
    }

    public void setDeleteVariableController(DeleteVariableController controller) {
        variableSection.setDeleteVariableController(controller);
    }

    public String getSelectedEvent() {
        return triggerManagerPanel.getSelectedEvent();
    }

    public String getSelectedKey() {
        return triggerManagerPanel.getSelectedKey();
    }

    public java.util.List<String> getConditionTexts() {
        return triggerManagerPanel.getConditionTexts();
    }

    public java.util.List<String> getActionTexts() {
        return triggerManagerPanel.getActionTexts();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension naturalSize = super.getPreferredSize();

        return new Dimension(290, naturalSize.height);
    }
}