package view;

import interface_adapter.Sprites.ImportSpriteFromUnsplashController;
import interface_adapter.Sprites.ImportSpriteFromUnsplashState;
import interface_adapter.Sprites.ImportSpriteFromUnsplashViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View for importing sprites from Unsplash.
 * This view allows users to search for images on Unsplash and import them as sprites.
 */
public class ImportSpriteFromUnsplashView extends JPanel implements PropertyChangeListener {

    private final String viewName = "importSpriteFromUnsplash";
    private final ImportSpriteFromUnsplashViewModel viewModel;
    private final ImportSpriteFromUnsplashController controller;

    // UI Components
    private final JTextField searchQueryField;
    private final JTextField imageIdField;
    private final JTextField fileNameField;
    private final JButton searchButton;
    private final JButton importByIdButton;
    private final JLabel statusLabel;
    private final JProgressBar progressBar;

    public ImportSpriteFromUnsplashView(ImportSpriteFromUnsplashViewModel viewModel,
                                         ImportSpriteFromUnsplashController controller) {
        this.viewModel = viewModel;
        this.controller = controller;
        this.viewModel.addPropertyChangeListener(this);

        // Set up the panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Import Sprite from Unsplash");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Import Options"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Search Query Section
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Query:"));
        searchQueryField = new JTextField(20);
        searchPanel.add(searchQueryField);
        searchButton = new JButton("Search & Import");
        searchPanel.add(searchButton);

        // Image ID Section
        JPanel imageIdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imageIdPanel.add(new JLabel("Image ID:"));
        imageIdField = new JTextField(20);
        imageIdPanel.add(imageIdField);
        importByIdButton = new JButton("Import by ID");
        imageIdPanel.add(importByIdButton);

        // File Name Section
        JPanel fileNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fileNamePanel.add(new JLabel("File Name (optional):"));
        fileNameField = new JTextField(20);
        fileNamePanel.add(fileNameField);

        inputPanel.add(searchPanel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(imageIdPanel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(fileNamePanel);

        // Status Panel
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Status"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        statusLabel = new JLabel("Ready to import sprites from Unsplash");
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setVisible(false);
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        statusPanel.add(statusLabel);
        statusPanel.add(Box.createVerticalStrut(10));
        statusPanel.add(progressBar);

        // Add panels to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        // Add action listeners
        searchButton.addActionListener(e -> handleSearchAndImport());
        importByIdButton.addActionListener(e -> handleImportById());

        // Add enter key listeners
        searchQueryField.addActionListener(e -> handleSearchAndImport());
        imageIdField.addActionListener(e -> handleImportById());
    }

    private void handleSearchAndImport() {
        String searchQuery = searchQueryField.getText().trim();
        String fileName = fileNameField.getText().trim();

        if (searchQuery.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a search query",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Set loading state
        setLoading(true);
        statusLabel.setText("Searching for images...");

        // Execute the use case in a background thread to keep UI responsive
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                controller.execute(searchQuery, fileName.isEmpty() ? null : fileName);
                return null;
            }
        };
        worker.execute();
    }

    private void handleImportById() {
        String imageId = imageIdField.getText().trim();
        String fileName = fileNameField.getText().trim();

        if (imageId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter an image ID",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Set loading state
        setLoading(true);
        statusLabel.setText("Importing image...");

        // Execute the use case in a background thread to keep UI responsive
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                controller.executeWithImageId(imageId, fileName.isEmpty() ? null : fileName);
                return null;
            }
        };
        worker.execute();
    }

    private void setLoading(boolean loading) {
        searchButton.setEnabled(!loading);
        importByIdButton.setEnabled(!loading);
        searchQueryField.setEnabled(!loading);
        imageIdField.setEnabled(!loading);
        fileNameField.setEnabled(!loading);
        progressBar.setVisible(loading);
        progressBar.setIndeterminate(loading);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ImportSpriteFromUnsplashViewModel.IMPORT_SPRITE_FROM_UNSPLASH_PROPERTY)) {
            ImportSpriteFromUnsplashState state = (ImportSpriteFromUnsplashState) evt.getNewValue();
            updateView(state);
        }
    }

    private void updateView(ImportSpriteFromUnsplashState state) {
        // Update loading state
        setLoading(state.isLoading());

        // Update status message
        statusLabel.setText(state.getMessage());

        // Show success or error dialog
        if (!state.isLoading()) {
            if (state.isSuccess()) {
                JOptionPane.showMessageDialog(this,
                        state.getMessage(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Clear input fields after successful import
                searchQueryField.setText("");
                imageIdField.setText("");
                fileNameField.setText("");
            } else if (!state.getMessage().isEmpty() && state.getMessage().startsWith("Error:")) {
                JOptionPane.showMessageDialog(this,
                        state.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public String getViewName() {
        return viewName;
    }
}

