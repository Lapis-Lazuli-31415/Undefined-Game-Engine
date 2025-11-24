package view;

import interface_adapter.Sprites.ImportSpriteFromUnsplashController;
import interface_adapter.Sprites.ImportSpriteFromUnsplashState;
import interface_adapter.Sprites.ImportSpriteFromUnsplashViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * unsplash tab
 */
public class ImportSpriteFromUnsplashView extends JPanel implements PropertyChangeListener {

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
        this.controller = controller;
        viewModel.addPropertyChangeListener(this);

        // panel setup
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Import Sprite from Unsplash");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Import Options"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // search query section
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Query:"));
        searchQueryField = new JTextField(20);
        searchPanel.add(searchQueryField);
        searchButton = new JButton("Search & Import");
        searchPanel.add(searchButton);

        // image id section
        JPanel imageIdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imageIdPanel.add(new JLabel("Image ID:"));
        imageIdField = new JTextField(20);
        imageIdPanel.add(imageIdField);
        importByIdButton = new JButton("Import by ID");
        imageIdPanel.add(importByIdButton);

        // file rename section
        JPanel fileNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fileNamePanel.add(new JLabel("File Name (optional):"));
        fileNameField = new JTextField(20);
        fileNamePanel.add(fileNameField);

        inputPanel.add(searchPanel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(imageIdPanel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(fileNamePanel);

        // status panel
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

        // adding panels into main
        add(titlePanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        // action listeners
        searchButton.addActionListener(e -> handleSearchAndImport());
        importByIdButton.addActionListener(e -> handleImportById());

        // key listeners
        searchQueryField.addActionListener(e -> handleSearchAndImport());
        imageIdField.addActionListener(e -> handleImportById());
    }

    private void handleSearchAndImport() {
        String searchQuery = searchQueryField.getText().trim();
        String fileName = fileNameField.getText().trim();

        // loading state
        setLoading(true);
        statusLabel.setText("Searching for images...");

        // execute the use case in a background thread to keep UI responsive
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
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

        // loading state
        setLoading(true);
        statusLabel.setText("Importing image...");

        // execute the use case in a background thread to keep UI responsive
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
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
        // update loading state
        setLoading(state.isLoading());

        // update status message
        statusLabel.setText(state.getMessage());

        // success or error
        if (!state.isLoading()) {
            if (state.isSuccess()) {
                JOptionPane.showMessageDialog(this,
                        state.getMessage(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // input clearing
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

    public String getViewName() {       // TODO: remove i think
        return "importSpriteFromUnsplash";
    }
}

