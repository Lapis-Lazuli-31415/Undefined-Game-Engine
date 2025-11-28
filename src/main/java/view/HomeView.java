package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

import entity.GameObject;
import entity.Scene;
import entity.Transform;
import entity.scripting.TriggerManager;
import entity.scripting.environment.Environment;
import interface_adapter.EditorState;
import interface_adapter.preview.PreviewController;
import interface_adapter.preview.PreviewState;
import interface_adapter.preview.PreviewViewModel;
import interface_adapter.transform.TransformViewModel;
import interface_adapter.transform.TransformController;
import app.TransformUseCaseFactory;
import use_case.Sprites.Import.ImportSpriteInteractor;
//// ===== ADDED BY CHENG: Imports for preview functionality =====
//
//import entity.scripting.environment.Environment;
//import interface_adapter.preview.PreviewController;
//import interface_adapter.preview.PreviewViewModel;
//import interface_adapter.preview.PreviewState;
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
//// ===== END ADDED BY CHENG =====

public class HomeView extends javax.swing.JFrame {

    // ====== FIELDS ======
    private JPanel leftSidebar;
    private JPanel assetsPanel;
    private JPanel gameComponentsPanel;
    private JPanel centerPanel;
    private JPanel propertiesPanel;

    // asset manager
    private interface_adapter.assets.AssetLibViewModel assetLibViewModel;
    private JPanel spritesContent;
    private JButton spritesAddButton;

    // sprite import
    private interface_adapter.Sprites.ImportSpriteController importSpriteController;
    private interface_adapter.Sprites.ImportSpriteViewModel importSpriteViewModel;

    // Demo wiring
    private ScenePanel scenePanel;
    private static GameObject DEMO_OBJECT = new GameObject(
            "demo-1",
            "Demo Sprite",
            true,
            new ArrayList<>(),
            null,
            new Transform(new Vector<Double>(Arrays.asList(0.0, 0.0)), 0f, new Vector<Double>(Arrays.asList(1.0, 1.0))),
            new TriggerManager()
    );
    private TransformViewModel transformViewModel;
    private TransformController transformController;
    // ===== ADDED BY CHENG: Preview system fields =====
    private PreviewController previewController;
    private PreviewViewModel previewViewModel;
    private PreviewWindow currentPreview;  // Track current preview window
    private EditorState editorState;
// ===== END ADDED BY CHENG =====

    // TODO: Delete this after gameObject selection is implemented
    public static GameObject getDemoGameObject() {
        return DEMO_OBJECT;
    }

    public HomeView(EditorState editorState) {
        this(new interface_adapter.assets.AssetLibViewModel(new entity.AssetLib()));
        this.editorState = editorState;
        initializePreviewSystem();  // ===== ADDED BY CHENG =====
    }

    public HomeView(interface_adapter.assets.AssetLibViewModel assetLibViewModel) {
        this.assetLibViewModel = assetLibViewModel;

        wireImportSpriteUseCase();
        loadExistingAssets();
        initComponents();
        setupAssetLibListener();
        setupImportSpriteListener();
    }

    private void wireImportSpriteUseCase() {
        try {
            // init DAO
            data_access.FileSystemSpriteDataAccessObject spriteDAO =
                new data_access.FileSystemSpriteDataAccessObject();

            // create view model
            importSpriteViewModel = new interface_adapter.Sprites.ImportSpriteViewModel();

            // create presenter
            interface_adapter.Sprites.ImportSpritePresenter presenter =
                new interface_adapter.Sprites.ImportSpritePresenter(importSpriteViewModel, assetLibViewModel);

            // create interactor
            ImportSpriteInteractor interactor =
                new ImportSpriteInteractor(
                    spriteDAO,
                    presenter,
                    assetLibViewModel.getAssetLib()
                );

            // create controller
            importSpriteController = new interface_adapter.Sprites.ImportSpriteController(interactor);

        } catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(null,
                "Failed to initialize sprite import: " + e.getMessage(),
                "Initialization Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupImportSpriteListener() {
        importSpriteViewModel.addPropertyChangeListener(evt -> {
            if (interface_adapter.Sprites.ImportSpriteViewModel.IMPORT_SPRITE_PROPERTY.equals(evt.getPropertyName())) {
                interface_adapter.Sprites.ImportSpriteState state =
                    (interface_adapter.Sprites.ImportSpriteState) evt.getNewValue();

                if (state.isSuccess()) {
                    JOptionPane.showMessageDialog(this,
                        state.getMessage(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                        state.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        // ====== MAIN FRAME SETTINGS ======
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Game Editor");
        setPreferredSize(new java.awt.Dimension(1300, 700));

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
        // NOTE: use the fields, not new local variables
        leftSidebar = new JPanel();
        leftSidebar.setLayout(new BoxLayout(leftSidebar, BoxLayout.Y_AXIS));
        leftSidebar.setPreferredSize(new Dimension(200, 700));
        leftSidebar.setBackground(new Color(45, 45, 45));

        // ====== ASSETS PANEL ======
        assetsPanel = new JPanel();
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

        spritesAddButton = new JButton("+");
        spritesAddButton.setMargin(new Insets(0, 4, 0, 4));
        spritesAddButton.addActionListener(e -> openLocalSpriteImport());

        spritesHeader.add(spritesLabel, BorderLayout.WEST);
        spritesHeader.add(spritesAddButton, BorderLayout.EAST);

        // gallery grid view NEED TO ADD SCROLLING
        spritesContent = new JPanel();
        spritesContent.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        spritesContent.setBackground(new Color(70, 70, 70));

        JScrollPane spritesScroll = new JScrollPane(spritesContent);
        spritesScroll.setPreferredSize(new Dimension(180, 140));

        assetsPanel.add(spritesHeader);
        assetsPanel.add(spritesScroll);
        assetsPanel.add(Box.createVerticalStrut(8));

//        // ---- AUDIO SCROLL PANEL ---- remove for now, might add back later
//        JPanel audioHeader = new JPanel(new BorderLayout());
//        audioHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
//        audioHeader.setOpaque(false);
//
//        JLabel audioLabel = new JLabel("Audio");
//        audioLabel.setForeground(Color.WHITE);
//
//        JButton audioAddButton = new JButton("+");
//        audioAddButton.setMargin(new Insets(0, 4, 0, 4));
//
//        audioHeader.add(audioLabel, BorderLayout.WEST);
//        audioHeader.add(audioAddButton, BorderLayout.EAST);
//
//        JPanel audioContent = new JPanel();
//        audioContent.setLayout(new BoxLayout(audioContent, BoxLayout.Y_AXIS));
//        audioContent.setBackground(new Color(70, 70, 70));
//
//        JScrollPane audioScroll = new JScrollPane(audioContent);
//        audioScroll.setPreferredSize(new Dimension(180, 140));
//
//        assetsPanel.add(audioHeader);
//        assetsPanel.add(audioScroll);

        // ====== Game Components PANEL ======
        gameComponentsPanel = new JPanel();
        gameComponentsPanel.setLayout(new BorderLayout());
        gameComponentsPanel.setBorder(BorderFactory.createTitledBorder("Game Components"));
        gameComponentsPanel.add(new JTextField("Search Components"), BorderLayout.NORTH);

        // ====== Assemble Sidebar ======
        leftSidebar.add(assetsPanel);
        leftSidebar.add(Box.createVerticalStrut(10));
        leftSidebar.add(gameComponentsPanel);

        // ====== CENTER PANEL ======
        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        // Top tab bar
        JPanel tabBar = new JPanel(new BorderLayout());
        tabBar.setPreferredSize(new Dimension(0, 35));
        tabBar.setBackground(new Color(60, 60, 60));

        JLabel tabLabel = new JLabel("   Start");
        tabLabel.setForeground(Color.WHITE);

//        JButton addTabButton = new JButton("+");
//        addTabButton.setPreferredSize(new Dimension(45, 35));

        JPanel rightTabControls = new JPanel();
        rightTabControls.setOpaque(false);

        JButton playButton = new JButton("▶");
        playButton.setBackground(new Color(0, 180, 0));   // green
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.setOpaque(true);
        playButton.setBorderPainted(false);

        JButton stopButton = new JButton("■");
        stopButton.setBackground(new Color(200, 0, 0));   // red
        stopButton.setForeground(Color.WHITE);
        stopButton.setFocusPainted(false);
        stopButton.setOpaque(true);
        stopButton.setBorderPainted(false);

        playButton.addActionListener(e -> onPlayClicked());
        stopButton.addActionListener(e -> onStopClicked());

        rightTabControls.add(playButton);
        rightTabControls.add(stopButton);

        tabBar.add(tabLabel, BorderLayout.WEST);
//      tabBar.add(addTabButton, BorderLayout.CENTER);
        tabBar.add(rightTabControls, BorderLayout.EAST);

        // Center placeholder content (Open Folder)
        JPanel openFolderPanel = new JPanel();
        openFolderPanel.setLayout(new BoxLayout(openFolderPanel, BoxLayout.Y_AXIS));
        openFolderPanel.setBackground(new Color(55, 55, 55));

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
        propertiesPanel = new PropertiesPanel();

        // Wrap in a scroll pane so the whole properties area scrolls
        JScrollPane propertiesScroll = new JScrollPane(propertiesPanel);
        propertiesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        propertiesScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        propertiesScroll.getVerticalScrollBar().setUnitIncrement(16);
        propertiesScroll.getViewport().setBackground(new Color(45, 45, 45));
        propertiesScroll.setBorder(null); // keep the nice "Properties" border from the inner panel


        // ====== DEMO ENTITY + LAYERS WIRING ======
        java.util.Vector<Double> pos = new java.util.Vector<>();
        pos.add(0.0); // x
        pos.add(0.0); // y

        java.util.Vector<Double> scale = new java.util.Vector<>();
        scale.add(1.0); // scaleX
        scale.add(1.0); // scaleY

        Transform transform = new Transform(pos, 0f, scale);

        DEMO_OBJECT.setTransform(transform);

        // Create view model
        transformViewModel = new TransformViewModel();

        // Use app-layer factory to wire up use case
        transformController = TransformUseCaseFactory.create(DEMO_OBJECT, transformViewModel);

        // Hook up ScenePanel to viewModel (Observer)
        scenePanel = new ScenePanel(transformViewModel);

        transformController.updateTransform(
                transform.getX(),
                transform.getY(),
                transform.getScaleX(),
                transform.getRotation()
        );

        centerPanel.remove(openFolderPanel);
        centerPanel.add(scenePanel, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();

        if (propertiesPanel instanceof PropertiesPanel props) {
            props.bind(
                    transformViewModel,
                    transformController,
                    () -> scenePanel.repaint()
            );
        }



        centerPanel.remove(openFolderPanel);
        centerPanel.add(scenePanel, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();

        // Bind properties panel to VM + controller
        if (propertiesPanel instanceof PropertiesPanel props) {
            props.bind(
                    transformViewModel,
                    transformController,
                    () -> scenePanel.repaint()
            );
        }

        getContentPane().add(leftSidebar, BorderLayout.WEST);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(propertiesPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
    }

    private void setupAssetLibListener() {
        assetLibViewModel.addPropertyChangeListener(evt -> {
            if (interface_adapter.assets.AssetLibViewModel.ASSET_ADDED.equals(evt.getPropertyName())) {
                entity.Asset newAsset = (entity.Asset) evt.getNewValue();
                if (newAsset instanceof entity.Image) {
                    addSpriteToUI((entity.Image) newAsset);
                }
            }
        });

        // display existing assets
        for (entity.Asset asset : assetLibViewModel.getAssetLib().getAll()) {
            if (asset instanceof entity.Image) {
                addSpriteToUI((entity.Image) asset);
            }
        }
    }

    private void addSpriteToUI(entity.Image image) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setPreferredSize(new Dimension(80, 100));
        cardPanel.setBackground(new Color(60, 60, 60));
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));

        // load + scale the image thumbnail
        try {
            java.awt.image.BufferedImage originalImage = javax.imageio.ImageIO.read(
                new java.io.File(image.getLocalpath().toString())
            );

            // calc thumbnail size
            int thumbWidth = 70;
            int thumbHeight = 70;
            double aspectRatio = (double) originalImage.getWidth() / originalImage.getHeight();

            if (aspectRatio > 1) {
                thumbHeight = (int) (thumbWidth / aspectRatio);
            } else {
                thumbWidth = (int) (thumbHeight * aspectRatio);
            }

            // scale image
            java.awt.Image scaledImage = originalImage.getScaledInstance(
                thumbWidth, thumbHeight, java.awt.Image.SCALE_SMOOTH
            );

            // create image label with centered icon
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(80, 70));

            cardPanel.add(imageLabel, BorderLayout.CENTER);

        } catch (Exception e) {
            // fallback if image fails to load
            JLabel iconLabel = new JLabel("image");
            iconLabel.setFont(new Font("Arial", Font.PLAIN, 30));
            iconLabel.setForeground(Color.WHITE);
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
            cardPanel.add(iconLabel, BorderLayout.CENTER);
        }

        // add name label to the bottom
        String displayName = image.getName();
        if (displayName.length() > 10) {
            displayName = displayName.substring(0, 8) + "...";
        }
        final JLabel nameLabel = new JLabel(displayName);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 9));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        cardPanel.add(nameLabel, BorderLayout.SOUTH);

        // Add hover effect
        cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cardPanel.setBackground(new Color(80, 80, 80));
                cardPanel.setBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 2));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                cardPanel.setBackground(new Color(60, 60, 60));
                cardPanel.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
            }

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.out.println("Selected sprite: " + image.getName());
                if (scenePanel != null) {
                    scenePanel.addOrSelectSprite(image);
                }
            }
        });

        cardPanel.setToolTipText(image.getName());

        spritesContent.add(cardPanel);
        spritesContent.revalidate();
        spritesContent.repaint();
    }

    private void loadExistingAssets() {
        try {
            // Create DAO to access uploads directory
            data_access.FileSystemSpriteDataAccessObject spriteDAO =
                    new data_access.FileSystemSpriteDataAccessObject();

            // Get all existing image files
            java.util.List<java.io.File> existingImages = spriteDAO.getAllExistingImages();

            // Load each image into the asset library
            for (java.io.File imageFile : existingImages) {
                try {
                    // Create Image entity from file path
                    entity.Image image = new entity.Image(imageFile.toPath());

                    // Add to asset library
                    assetLibViewModel.getAssetLib().add(image);
                }
                catch (Exception e) {
                    // Log error but continue loading other images
                    System.err.println("Failed to load image: " + imageFile.getName() + " - " + e.getMessage());
                }
            }
        }
        catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to load existing sprites: " + e.getMessage(),
                    "Loading Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }


    private void openLocalSpriteImport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Import Sprite");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Image Files", "jpg", "jpeg", "png"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            // call controller to import sprite
            importSpriteController.importSprite(selectedFile);
        }
    }

    public interface_adapter.assets.AssetLibViewModel getAssetLibViewModel() {
        return assetLibViewModel;
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
     * Get current scene from EditorState
     * ADDED BY CHENG for Use Case 5: Preview/Testing Feature
     */
    private Scene getCurrentScene() {
        if (editorState == null) {
            return null;
        }
        return editorState.getCurrentScene();
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
        return env;
    }
    // ========== END PREVIEW SYSTEM METHODS - ADDED BY CHENG ==========

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EditorState editorState = new EditorState();
                new HomeView(editorState).setVisible(true);
            }
        });
    }
}
