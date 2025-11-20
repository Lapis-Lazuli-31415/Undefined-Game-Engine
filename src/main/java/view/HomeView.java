package view;

import java.awt.*;
import javax.swing.*;

import entity.GameObject;
import entity.Transform;
import interface_adapter.transform.TransformViewModel;
import interface_adapter.transform.TransformPresenter;
import interface_adapter.transform.TransformController;
import use_case.transform.UpdateTransformInteractor;

// ===== ADDED BY CHENG: Imports for preview functionality =====
import entity.Scene;
import entity.scripting.environment.Environment;
import interface_adapter.preview.PreviewController;
import interface_adapter.preview.PreviewViewModel;
import interface_adapter.preview.PreviewState;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
// ===== END ADDED BY CHENG =====

public class HomeView extends javax.swing.JFrame {

    // ====== FIELDS ======
    private JPanel leftSidebar;
    private JPanel assetsPanel;
    private JPanel filesystemPanel;
    private JPanel centerPanel;
    private JPanel propertiesPanel;

    // Demo wiring
    private ScenePanel scenePanel;
    private GameObject demoObject;
    private TransformViewModel transformViewModel;
    private TransformController transformController;

    // ===== ADDED BY CHENG: Preview system fields =====
    private PreviewController previewController;
    private PreviewViewModel previewViewModel;
    private PreviewWindow currentPreview;  // Track current preview window
// ===== END ADDED BY CHENG =====

    public HomeView() {
        initComponents();
        initializePreviewSystem();  // ===== ADDED BY CHENG =====
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        // ====== MAIN FRAME SETTINGS ======
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Game Editor");
        setPreferredSize(new java.awt.Dimension(1200, 700));

        getContentPane().setLayout(new BorderLayout());

        // ====== MENU BAR ======
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new JMenu("Project"));
        menuBar.add(new JMenu("Scene"));
        menuBar.add(new JMenu("Debug"));
        menuBar.add(new JMenu("Save"));
        menuBar.add(new JMenu("Help"));
        setJMenuBar(menuBar);

        // ====== LEFT SIDEBAR (Assets + Filesystem) ======
        JPanel leftSidebar = new JPanel();
        leftSidebar.setLayout(new BoxLayout(leftSidebar, BoxLayout.Y_AXIS));
        leftSidebar.setPreferredSize(new Dimension(200, 700));
        leftSidebar.setBackground(new Color(45, 45, 45));

        // ====== ASSETS PANEL ======
        JPanel assetsPanel = new JPanel();
        assetsPanel.setLayout(new BoxLayout(assetsPanel, BoxLayout.Y_AXIS));
        assetsPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 1),
                        "Assets",
                        javax.swing.border.TitledBorder.LEADING,
                        javax.swing.border.TitledBorder.TOP,
                        null,
                        Color.WHITE // Title text color
                )
        );
        assetsPanel.setBackground(new Color(55, 55, 55));

        // ---- SPRITES SCROLL PANEL ----
        JPanel spritesHeader = new JPanel(new BorderLayout());
        spritesHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        spritesHeader.setOpaque(false);

        JLabel spritesLabel = new JLabel("Sprites");
        spritesLabel.setForeground(Color.WHITE);

        JButton spritesAddButton = new JButton("+");
        spritesAddButton.setMargin(new Insets(0, 4, 0, 4));

        spritesHeader.add(spritesLabel, BorderLayout.WEST);
        spritesHeader.add(spritesAddButton, BorderLayout.EAST);

        // The list inside the scroll (placeholder)
        JPanel spritesContent = new JPanel();
        spritesContent.setLayout(new BoxLayout(spritesContent, BoxLayout.Y_AXIS));
        spritesContent.setBackground(new Color(70, 70, 70));

        JScrollPane spritesScroll = new JScrollPane(spritesContent);
        spritesScroll.setPreferredSize(new Dimension(180, 140));

        // Add header + scroll
        assetsPanel.add(spritesHeader);
        assetsPanel.add(spritesScroll);
        assetsPanel.add(Box.createVerticalStrut(8));


        // ---- AUDIO SCROLL PANEL ----
        JPanel audioHeader = new JPanel(new BorderLayout());
        audioHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        audioHeader.setOpaque(false);

        JLabel audioLabel = new JLabel("Audio");
        audioLabel.setForeground(Color.WHITE);

        JButton audioAddButton = new JButton("+");
        audioAddButton.setMargin(new Insets(0, 4, 0, 4));

        audioHeader.add(audioLabel, BorderLayout.WEST);
        audioHeader.add(audioAddButton, BorderLayout.EAST);

        // The list inside the scroll (placeholder)
        JPanel audioContent = new JPanel();
        audioContent.setLayout(new BoxLayout(audioContent, BoxLayout.Y_AXIS));
        audioContent.setBackground(new Color(70, 70, 70));

        JScrollPane audioScroll = new JScrollPane(audioContent);
        audioScroll.setPreferredSize(new Dimension(180, 140));

        // Add header + scroll
        assetsPanel.add(audioHeader);
        assetsPanel.add(audioScroll);


        // ====== FILESYSTEM PANEL ======
        JPanel filesystemPanel = new JPanel();
        filesystemPanel.setLayout(new BorderLayout());
        filesystemPanel.setBorder(BorderFactory.createTitledBorder("FileSystem"));
        filesystemPanel.add(new JTextField("Search Files"), BorderLayout.NORTH);

        // ====== Assemble Sidebar ======
        leftSidebar.add(assetsPanel);
        leftSidebar.add(Box.createVerticalStrut(10));
        leftSidebar.add(filesystemPanel);


        // ====== CENTER PANEL ======
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        // Top tab bar
        JPanel tabBar = new JPanel(new BorderLayout());
        tabBar.setPreferredSize(new Dimension(0, 35));
        tabBar.setBackground(new Color(60,60,60));

        JLabel tabLabel = new JLabel("   Start");
        tabLabel.setForeground(Color.WHITE);

        JButton addTabButton = new JButton("+");
        addTabButton.setPreferredSize(new Dimension(45,35));

        JPanel rightTabControls = new JPanel();
        rightTabControls.setOpaque(false);

        JButton playButton = new JButton("▶");
        playButton.setBackground(new Color(0, 180, 0));   // green
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.setOpaque(true);
        playButton.setBorderPainted(false);
        playButton.addActionListener(e -> onPlayClicked());  // ===== MODIFIED BY CHENG =====

        JButton stopButton = new JButton("■");
        stopButton.setBackground(new Color(200, 0, 0));   // red
        stopButton.setForeground(Color.WHITE);
        stopButton.setFocusPainted(false);
        stopButton.setOpaque(true);
        stopButton.setBorderPainted(false);
        stopButton.addActionListener(e -> onStopClicked());  // ===== MODIFIED BY CHENG =====


        rightTabControls.add(playButton);
        rightTabControls.add(stopButton);

        tabBar.add(tabLabel, BorderLayout.WEST);
        tabBar.add(addTabButton, BorderLayout.CENTER);
        tabBar.add(rightTabControls, BorderLayout.EAST);

        // Center content
        JPanel openFolderPanel = new JPanel();
        openFolderPanel.setLayout(new BoxLayout(openFolderPanel, BoxLayout.Y_AXIS));
        openFolderPanel.setBackground(new Color(55,55,55));

        JLabel folderIcon = new JLabel("\uD83D\uDCC1"); // folder emoji
        folderIcon.setFont(new Font("Arial", Font.PLAIN, 120));
        folderIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel openFolderLabel = new JLabel("Open Folder");
        openFolderLabel.setFont(new Font("Arial", Font.BOLD, 28));
        openFolderLabel.setForeground(Color.WHITE);
        openFolderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        openFolderPanel.add(Box.createVerticalGlue());
        openFolderPanel.add(folderIcon);
        openFolderPanel.add(Box.createVerticalStrut(20));
        openFolderPanel.add(openFolderLabel);
        openFolderPanel.add(Box.createVerticalGlue());

        centerPanel.add(tabBar, BorderLayout.NORTH);
        centerPanel.add(openFolderPanel, BorderLayout.CENTER);

        // ====== RIGHT PROPERTIES PANEL ======
        JPanel propertiesPanel = new JPanel();
        propertiesPanel.setLayout(new BorderLayout());
        propertiesPanel.setPreferredSize(new Dimension(200, 700));
        propertiesPanel.setBorder(BorderFactory.createTitledBorder("Properties"));

        JLabel propertiesPlaceholder = new JLabel("Select Object");
        propertiesPlaceholder.setHorizontalAlignment(JLabel.CENTER);
        propertiesPanel.add(propertiesPlaceholder, BorderLayout.CENTER);

        // ====== ADDING PANELS TO FRAME ======
        getContentPane().add(leftSidebar, BorderLayout.WEST);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(propertiesPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
    }

    // ========== PREVIEW SYSTEM METHODS - ADDED BY CHENG ==========

    /**
     * Initialize the preview system
     * ADDED BY CHENG for Use Case 5: Preview/Testing Feature
     *
     * Sets up Clean Architecture components:
     * - ViewModel (observable state)
     * - Presenter (formats Use Case output)
     * - Interactor (business logic)
     * - Controller (receives View input)
     */
    private void initializePreviewSystem() {
        // Create Use Case components
        use_case.validate_scene.ValidateSceneInteractor validator =
                new use_case.validate_scene.ValidateSceneInteractor();

        // Create Interface Adapter components
        previewViewModel = new PreviewViewModel();
        interface_adapter.preview.PreviewPresenter presenter =
                new interface_adapter.preview.PreviewPresenter(previewViewModel);

        // Create Use Case Interactor
        use_case.preview.PreviewInteractor interactor =
                new use_case.preview.PreviewInteractor(validator, presenter);

        // Create Controller
        previewController = new PreviewController(interactor);

        // Register this view as observer of ViewModel
        previewViewModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("state".equals(evt.getPropertyName())) {
                    handlePreviewStateChange((PreviewState) evt.getNewValue());
                }
            }
        });

        currentPreview = null;
    }

    /**
     * Handle Play button click
     * ADDED BY CHENG for Use Case 5: Preview/Testing Feature
     *
     * Following Clean Architecture:
     * 1. Get current scene (UI responsibility)
     * 2. Delegate to Controller (Interface Adapter)
     * 3. Controller → Interactor → Presenter → ViewModel
     * 4. View observes ViewModel and updates UI
     */
    private void onPlayClicked() {
        try {
            // 1. Get current scene (UI responsibility)
            Scene scene = getCurrentScene();

            if (scene == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No scene is currently open.\nPlease open or create a scene first.",
                        "Cannot Start Preview",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // 2. Delegate to Controller (Clean Architecture flow starts here)
            previewController.execute(scene);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to start preview: " + ex.getMessage(),
                    "Preview Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Handle Stop button click
     * ADDED BY CHENG for Use Case 5: Preview/Testing Feature
     *
     * Stops the game loop and closes the preview window
     */
    private void onStopClicked() {
        if (currentPreview != null) {
            // Stop the preview
            currentPreview.close();
            currentPreview = null;

            System.out.println("⏹ Preview stopped");
        } else {
            System.out.println("No preview is running");
        }
    }

    /**
     * Handle preview state changes from ViewModel
     * ADDED BY CHENG for Use Case 5: Preview/Testing Feature
     *
     * This method is called when ViewModel updates (Observer pattern)
     * Updates UI based on the new state
     */
    private void handlePreviewStateChange(PreviewState state) {
        // Handle error
        if (state.getError() != null) {
            JOptionPane.showMessageDialog(
                    this,
                    state.getError(),
                    "Cannot Start Preview",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Handle warning
        if (state.getWarning() != null) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    state.getWarning() + "\n\nContinue anyway?",
                    "Preview Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // Open preview if ready
        if (state.isReadyToPreview() && state.getScene() != null) {
            Environment globalEnv = createGlobalEnvironment();
            currentPreview = new PreviewWindow(state.getScene(), globalEnv);
            currentPreview.display();
            System.out.println("✅ Preview started");
        }
    }

    /**
     * Get current scene from scene manager
     * ADDED BY CHENG for Use Case 5: Preview/Testing Feature
     *
     * TODO: Replace with actual SceneManager implementation when available
     * Currently returns a test scene for development
     */
    private Scene getCurrentScene() {
        // TODO: Replace with actual implementation
        // Example: return sceneManager.getCurrentScene();

        // For testing: create a test scene using Lynn's demoObject
        return createTestScene();
    }

    /**
     * Create global environment for triggers
     * ADDED BY CHENG for Use Case 5: Preview/Testing Feature
     *
     * The global environment stores variables that are shared across
     * all GameObjects (e.g., game score, level, player health)
     */
    private Environment createGlobalEnvironment() {
        Environment env = new Environment();

        // Add initial global variables here if needed
        // Example:
        // try {
        //     env.set("game", "score", Integer.class, 0);
        //     env.set("game", "level", Integer.class, 1);
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        return env;
    }

    /**
     * Create test scene (temporary implementation)
     * ADDED BY CHENG for Use Case 5: Preview/Testing Feature
     *
     * This method creates a test scene using Lynn's demoObject
     * TODO: Remove this when getCurrentScene() is properly implemented
     */
    private Scene createTestScene() {
        java.util.ArrayList<GameObject> objects = new java.util.ArrayList<>();

        // Reuse Lynn's demoObject for testing
        if (demoObject != null) {
            objects.add(demoObject);
        }

        return new Scene(
                "demo-scene",      // Scene ID
                "Demo Scene",      // Scene name
                objects,           // GameObjects list
                null               // Background music (null will trigger warning)
        );
    }

    // ========== END PREVIEW SYSTEM METHODS - ADDED BY CHENG ==========

    // ====== MAIN METHOD ======
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeView().setVisible(true);
            }
        });
    }
}
