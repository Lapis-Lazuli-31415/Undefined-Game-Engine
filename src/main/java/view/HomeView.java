package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.Vector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;


import entity.GameObject;
import entity.Scene;
import entity.Transform;
import javax.swing.tree.DefaultTreeModel;

import entity.GameObject;
import entity.Scene;
import entity.Transform;
import interface_adapter.EditorState;
import entity.*;
import entity.scripting.TriggerManager;
import entity.scripting.environment.Environment;
import interface_adapter.EditorState;
import interface_adapter.preview.PreviewController;
import interface_adapter.preview.PreviewState;
import interface_adapter.preview.PreviewViewModel;
import interface_adapter.transform.TransformViewModel;
import interface_adapter.transform.TransformController;
import app.TransformUseCaseFactory;
import app.VariableUseCaseFactory;
import interface_adapter.trigger.TriggerManagerViewModel;
import interface_adapter.variable.delete.DeleteVariableController;
import interface_adapter.variable.update.UpdateVariableController;
import interface_adapter.variable.get.GetAllVariablesController;
import interface_adapter.variable.GlobalVariableViewModel;
import interface_adapter.variable.LocalVariableViewModel;


import entity.SpriteRenderer;
import entity.scripting.Trigger;


import entity.SpriteRenderer;
import entity.scripting.Trigger;

import view.property.PropertiesPanel;

import data_access.saving.JsonProjectDataAccess;
import use_case.saving.SaveProjectInteractor;
import interface_adapter.saving.SaveProjectController;
import interface_adapter.saving.SaveProjectPresenter;
import interface_adapter.saving.SaveProjectViewModel;
import interface_adapter.saving.SaveProjectState;
import view.util.PropertyPanelUtility;

public class HomeView extends javax.swing.JFrame {

    // ====== FIELDS ======
    private JPanel leftSidebar;
    private JPanel assetsPanel;
    private JPanel gameComponentsPanel;
    private JPanel centerPanel;
    private JPanel propertiesPanel;

    // Entities for Project Structure
    private final Project currentProject;
    private final Scene currentScene;
    private SaveProjectController saveProjectController;
    private SaveProjectViewModel saveViewModel;

    // Save Status UI
    private JLabel saveStatusLabel;

    private Environment globalEnvironment;

    // asset manager
    private final interface_adapter.assets.AssetLibViewModel assetLibViewModel;
    private JPanel spritesContent;
    private JButton spritesAddButton;

    // sprite import
    private interface_adapter.sprites.ImportSpriteController importSpriteController;
    private interface_adapter.sprites.ImportSpriteViewModel importSpriteViewModel;

    // unsplash import
    private interface_adapter.sprites.ImportSpriteFromUnsplashController unsplashController;
    private interface_adapter.sprites.ImportSpriteFromUnsplashViewModel unsplashViewModel;
    private ImportSpriteFromUnsplashView unsplashView;

    // trigger manager
    private TriggerManagerViewModel triggerManagerViewModel;

    // variable management
    private GlobalVariableViewModel globalVariableViewModel;
    private LocalVariableViewModel localVariableViewModel;
    private UpdateVariableController updateVariableController;
    private DeleteVariableController deleteVariableController;
    private GetAllVariablesController getAllVariablesController;

    // Demo wiring
    private ScenePanel scenePanel;
    private static GameObject DEMO_OBJECT;
    private TransformViewModel transformViewModel;
    private TransformController transformController;
    // ===== ADDED BY CHENG: Preview system fields =====
    private PreviewController previewController;
    private PreviewViewModel previewViewModel;
    private PreviewWindow currentPreview;  // Track current preview window
    private EditorState editorState;
// ===== END ADDED BY CHENG =====

    public HomeView(
            interface_adapter.assets.AssetLibViewModel assetLibViewModel,
            interface_adapter.sprites.ImportSpriteController importSpriteController,
            interface_adapter.sprites.ImportSpriteViewModel importSpriteViewModel,
            interface_adapter.sprites.ImportSpriteFromUnsplashController unsplashController,
            interface_adapter.sprites.ImportSpriteFromUnsplashViewModel unsplashViewModel) {

        this.assetLibViewModel = assetLibViewModel;
        initializePreviewSystem();
        this.importSpriteController = importSpriteController;
        this.importSpriteViewModel = importSpriteViewModel;
        this.unsplashController = unsplashController;
        this.unsplashViewModel = unsplashViewModel;

        // 1. ATTEMPT TO LOAD PROJECT FROM JSON
        JsonProjectDataAccess dataAccess = new JsonProjectDataAccess();
        Project loadedProject = null;

        try {
            System.out.println("Attempting to load 'database.json'...");
            loadedProject = dataAccess.load("database.json");
            System.out.println("Project loaded successfully: " + loadedProject.getName());
        } catch (Exception e) {
            System.out.println("Could not load existing project (" + e.getMessage() + "). Creating new default project.");
        }

        if (loadedProject != null) {
            this.currentProject = loadedProject;

            if (this.currentProject.getGlobalEnvironment() != null) {
                this.globalEnvironment = this.currentProject.getGlobalEnvironment();
            } else {
                this.globalEnvironment = new Environment();
            }

            if (!this.currentProject.getScenes().isEmpty()) {
                this.currentScene = this.currentProject.getScenes().get(0);
            } else {
                this.currentScene = new Scene(UUID.randomUUID(), "Default Scene", new ArrayList<>());
                this.currentProject.getScenes().add(this.currentScene);
            }

            this.assetLibViewModel.setState(this.currentProject.getAssets());

            if (!this.currentScene.getGameObjects().isEmpty()) {
                DEMO_OBJECT = this.currentScene.getGameObjects().get(0);
                System.out.println("Selected object from save: " + DEMO_OBJECT.getName());
            } else {
                DEMO_OBJECT = createDefaultGameObject();
                this.currentScene.getGameObjects().add(DEMO_OBJECT);
            }

        } else {
            this.globalEnvironment = new Environment();
            GameController gameController = new GameController(this.globalEnvironment);
            this.currentScene = new Scene(UUID.randomUUID(), "Default Scene", new ArrayList<>());
            ArrayList<Scene> scenes = new ArrayList<>();
            scenes.add(this.currentScene);

            this.currentProject = new Project("proj-1", "My Game Project", scenes, assetLibViewModel.getAssetLib(), gameController);

            DEMO_OBJECT = createDefaultGameObject();
            this.currentScene.getGameObjects().add(DEMO_OBJECT);
        }

        // 2. INITIALIZE SAVE SYSTEM
        this.saveViewModel = new SaveProjectViewModel();

        SaveProjectPresenter savePresenter = new SaveProjectPresenter(this.saveViewModel);
        SaveProjectInteractor saveInteractor = new SaveProjectInteractor(dataAccess, savePresenter, currentProject);
        saveProjectController = new SaveProjectController(saveInteractor);

        // 3. SETUP SAVE LISTENER
        setupSaveListener();

        if (unsplashController != null && unsplashViewModel != null) {
            this.unsplashView = new ImportSpriteFromUnsplashView(unsplashViewModel, unsplashController);
        }

        // Trigger Manager Set up
        triggerManagerViewModel = new TriggerManagerViewModel();

        initComponents();
        setupAssetLibListener();
        setupImportSpriteListener();
    }

    private GameObject createDefaultGameObject() {
        return new GameObject(
                "demo-1", "Demo Sprite", true, null,
                new Transform(new Vector<>(Arrays.asList(0.0, 0.0)), 0f, new Vector<>(Arrays.asList(1.0, 1.0))),
                null, new TriggerManager()
        );
    }

    private void rewireLocalVariablesFor(GameObject target) {
        if (target == null) {
            target = DEMO_OBJECT;
        }

        Environment localEnv = target.getEnvironment();
        if (localEnv == null) {
            localEnv = new Environment();
            target.setEnvironment(localEnv);
        }

        VariableUseCaseFactory.VariableWiring wiring =
                VariableUseCaseFactory.create(globalEnvironment, localEnv, globalVariableViewModel);

        localVariableViewModel = wiring.getLocalViewModel();
        updateVariableController = wiring.getUpdateController();
        deleteVariableController = wiring.getDeleteController();
        getAllVariablesController = wiring.getGetAllController();

        if (propertiesPanel instanceof PropertiesPanel props) {
            props.setLocalVariableViewModel(localVariableViewModel);
            props.setVariableController(updateVariableController);
            props.setDeleteVariableController(deleteVariableController);
        }

        getAllVariablesController.refreshGlobalVariables();
        getAllVariablesController.refreshLocalVariables();
    }

    private void triggerAutoSave() {
        System.out.println("Auto-saving project...");
        saveProjectController.execute(currentProject.getName());
    }

    private void setupSaveListener() {
        this.saveViewModel.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName())) {
                SaveProjectState state = (SaveProjectState) evt.getNewValue();

                if (state.getError() == null) {
                    showSaveConfirmation();
                } else {
                    System.err.println("Save Error: " + state.getError());
                }
            }
        });
    }

    private void showSaveConfirmation() {
        if (saveStatusLabel != null) {
            saveStatusLabel.setVisible(true);
            Timer timer = new Timer(2000, e -> saveStatusLabel.setVisible(false));
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void setupImportSpriteListener() {
        importSpriteViewModel.addPropertyChangeListener(evt -> {
            if (interface_adapter.sprites.ImportSpriteViewModel.IMPORT_SPRITE_PROPERTY.equals(evt.getPropertyName())) {
                interface_adapter.sprites.ImportSpriteState state =
                        (interface_adapter.sprites.ImportSpriteState) evt.getNewValue();

                if (state.isSuccess()) {
                    JOptionPane.showMessageDialog(this,
                            state.getMessage(),
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
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
        setTitle("Game Editor - " + currentProject.getName());
        setPreferredSize(new java.awt.Dimension(1300, 700));

        getContentPane().setLayout(new BorderLayout());

        // ====== MENU BAR ======
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new JMenu("Project"));
        menuBar.add(new JMenu("Save"));
        menuBar.add(new JMenu("Help"));
        setJMenuBar(menuBar);

        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel homeTabContent = new JPanel(new BorderLayout());

        // ====== LEFT SIDEBAR (Assets + Filesystem) ======
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
                        Color.WHITE
                )
        );
        assetsPanel.setBackground(new Color(55, 55, 55));

        // ---- SPRITES SCROLL PANEL ----
        JPanel spritesHeader = new JPanel(new BorderLayout());
        spritesHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        spritesHeader.setOpaque(false);

        JLabel spritesLabel = new JLabel("Sprites");
        spritesLabel.setForeground(Color.WHITE);

        spritesAddButton = PropertyPanelUtility.createAddButton();
        spritesAddButton.addActionListener(e -> openSpriteImportMenu());

        spritesHeader.add(spritesLabel, BorderLayout.WEST);
        spritesHeader.add(spritesAddButton, BorderLayout.EAST);

        spritesContent = new JPanel();
        spritesContent.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        spritesContent.setBackground(new Color(70, 70, 70));

        JScrollPane spritesScroll = new JScrollPane(spritesContent);
        spritesScroll.setPreferredSize(new Dimension(180, 140));

        assetsPanel.add(spritesHeader);
        assetsPanel.add(spritesScroll);
        assetsPanel.add(Box.createVerticalStrut(8));

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

        JPanel rightTabControls = new JPanel();
        rightTabControls.setOpaque(false);
        rightTabControls.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        // --- SAVE STATUS LABEL ---
        saveStatusLabel = new JLabel("✔");
        saveStatusLabel.setForeground(new Color(100, 255, 100)); // Bright Green
        saveStatusLabel.setVisible(false);
        rightTabControls.add(saveStatusLabel);

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
        tabBar.add(rightTabControls, BorderLayout.EAST);

        // ====== RIGHT PROPERTIES PANEL ======
        propertiesPanel = new PropertiesPanel(triggerManagerViewModel);

        JScrollPane propertiesScroll = new JScrollPane(propertiesPanel);
        propertiesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        propertiesScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        propertiesScroll.getVerticalScrollBar().setUnitIncrement(16);
        propertiesScroll.getViewport().setBackground(new Color(45, 45, 45));
        propertiesScroll.setBorder(null);

        // ====== ENTITY + LAYERS WIRING ======
        Transform transform = DEMO_OBJECT.getTransform();
        if (transform == null) {
            java.util.Vector<Double> pos = new java.util.Vector<>(); pos.add(0.0); pos.add(0.0);
            java.util.Vector<Double> scale = new java.util.Vector<>(); scale.add(1.0); scale.add(1.0);
            transform = new Transform(pos, 0f, scale);
            DEMO_OBJECT.setTransform(transform);
        }

        transformViewModel = new TransformViewModel();
        transformController = TransformUseCaseFactory.create(DEMO_OBJECT, transformViewModel);

        scenePanel = new ScenePanel(transformViewModel, triggerManagerViewModel);
        scenePanel.setScene(currentScene);
        scenePanel.setOnSceneChangeCallback(() -> triggerAutoSave());

        transformController.updateTransform(
                transform.getX(),
                transform.getY(),
                transform.getScaleX(),
                transform.getRotation()
        );

        centerPanel.add(tabBar, BorderLayout.NORTH);
        centerPanel.add(scenePanel, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();

        // Wiring Properties Panel
        if (propertiesPanel instanceof PropertiesPanel props) {
            props.bindTransform(
                    transformViewModel,
                    transformController,
                    () -> {
                        scenePanel.repaint();
                        triggerAutoSave();
                    }
            );
            props.setAutoSaveCallback(() -> triggerAutoSave());
        }

        // rewire variables when selection changes
        scenePanel.setOnSelectionChangeCallback(() -> {
            GameObject selectedObject = scenePanel.getSelectedObject();

            // Rewire variables for the newly selected object
            rewireLocalVariablesFor(selectedObject);

            // Also update sprite renderer binding
            if (propertiesPanel instanceof PropertiesPanel props && selectedObject != null) {
                props.bindSpriteRenderer(selectedObject, assetLibViewModel, () -> {
                    scenePanel.repaint();
                    triggerAutoSave();
                });
            }
        });

        // Variable Wiring
        if (DEMO_OBJECT.getEnvironment() == null) {
            DEMO_OBJECT.setEnvironment(new Environment());
        }
        Environment localEnvironment = DEMO_OBJECT.getEnvironment();

        VariableUseCaseFactory.VariableWiring variableWiring =
                VariableUseCaseFactory.create(globalEnvironment, localEnvironment, null);

        globalVariableViewModel = variableWiring.getGlobalViewModel();
        localVariableViewModel = variableWiring.getLocalViewModel();
        updateVariableController = variableWiring.getUpdateController();
        deleteVariableController = variableWiring.getDeleteController();
        getAllVariablesController = variableWiring.getGetAllController();

        if (propertiesPanel instanceof PropertiesPanel props) {
            props.setLocalVariableViewModel(localVariableViewModel);
            props.setGlobalVariableViewModel(globalVariableViewModel);
            props.setVariableController(updateVariableController);
            props.setDeleteVariableController(deleteVariableController);
        }

        getAllVariablesController.refreshGlobalVariables();
        getAllVariablesController.refreshLocalVariables();

        homeTabContent.add(leftSidebar, BorderLayout.WEST);
        homeTabContent.add(centerPanel, BorderLayout.CENTER);
        homeTabContent.add(propertiesScroll, BorderLayout.EAST);

        tabbedPane.addTab("Home", homeTabContent);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);
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

        try {
            java.awt.image.BufferedImage originalImage = javax.imageio.ImageIO.read(
                    new java.io.File(image.getLocalpath().toString())
            );

            int thumbWidth = 70;
            int thumbHeight = 70;
            double aspectRatio = (double) originalImage.getWidth() / originalImage.getHeight();

            if (aspectRatio > 1) {
                thumbHeight = (int) (thumbWidth / aspectRatio);
            } else {
                thumbWidth = (int) (thumbHeight * aspectRatio);
            }

            java.awt.Image scaledImage = originalImage.getScaledInstance(
                    thumbWidth, thumbHeight, java.awt.Image.SCALE_SMOOTH
            );

            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(80, 70));

            cardPanel.add(imageLabel, BorderLayout.CENTER);

        } catch (Exception e) {
            JLabel iconLabel = new JLabel("image");
            iconLabel.setFont(new Font("Arial", Font.PLAIN, 30));
            iconLabel.setForeground(Color.WHITE);
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
            cardPanel.add(iconLabel, BorderLayout.CENTER);
        }

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

    private void openSpriteImportMenu() {
        JPopupMenu importMenu = new JPopupMenu();

        JMenuItem localImport = new JMenuItem("Import from Local");
        localImport.addActionListener(e -> openLocalSpriteImport());
        importMenu.add(localImport);

        if (unsplashView != null) {
            JMenuItem unsplashImport = new JMenuItem("Import from Unsplash");
            unsplashImport.addActionListener(e -> openUnsplashImportDialog());
            importMenu.add(unsplashImport);
        }

        importMenu.show(spritesAddButton, 0, spritesAddButton.getHeight());
    }

    private void openLocalSpriteImport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Import Sprite");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image Files", "jpg", "jpeg", "png"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            importSpriteController.importSprite(selectedFile);
        }
    }

    private void openUnsplashImportDialog() {
        if (unsplashView == null) {
            JOptionPane.showMessageDialog(this,
                    "Unsplash import is not available. Please check your API key configuration.",
                    "Feature Unavailable",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Import from Unsplash", true);
        dialog.setContentPane(unsplashView);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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
     *
     * CLEAN ARCHITECTURE COMPLIANCE:
     * - PreviewState only contains simple types (no Entity objects)
     * - View maintains its own reference to Scene for rendering
     * - Separation: Use Case validates → Presenter formats → View renders
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
        if (state.isReadyToPreview()) {
            // View gets Scene from its own source (not from PreviewState)
            Scene originalScene = getCurrentScene();

            if (originalScene == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Scene is no longer available",
                        "Preview Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Create deep copy for preview (isolation)
            Scene previewScene = originalScene.copy();

            // Create global environment and start preview
            Environment globalEnv = createGlobalEnvironment();
            currentPreview = new PreviewWindow(previewScene, globalEnv);
            currentPreview.display();

            System.out.println("✅ Preview started");
            System.out.println("   Scene: " + state.getSceneName() + " (" + state.getSceneId() + ")");
            System.out.println("   Objects: " + state.getGameObjectCount());
        }
    }

    /**
     * Get current scene from EditorState
     * ADDED BY CHENG for Use Case 5: Preview/Testing Feature
     */
    private Scene getCurrentScene() {
//        if (editorState == null) {
//            return null;
//        }
        return editorState.getCurrentScene();
//        return createTestScene();
    }
//private Scene createTestScene() {
//    Scene scene = Scene.create("DragonTamingDemo");
//    // ========== LOAD IMAGES ==========
//    entity.Image keyImage = loadDemoImage("/Users/chengguanru/Fall_CSC207/Undefined-Game-Engine2/src/main/resources/demo_assets/key.png");
//    entity.Image chestImage = loadDemoImage("/Users/chengguanru/Fall_CSC207/Undefined-Game-Engine2/src/main/resources/demo_assets/chest.png");
//    entity.Image dragonImage = loadDemoImage("/Users/chengguanru/Fall_CSC207/Undefined-Game-Engine2/src/main/resources/demo_assets/dragon.png");
//    // ========== 1. KEY 🔑 ==========
//    Transform keyTransform = new Transform(
//        new Vector<>(Arrays.asList(-200.0, -80.0)),
//        0f,
//        new Vector<>(Arrays.asList(0.1, 0.1))
//    );
//
//    SpriteRenderer keySprite = new SpriteRenderer(keyImage, true);
//    TriggerManager keyTriggerManager = new TriggerManager();
//
//    GameObject key = new GameObject(
//            "Key",
//            "Key",
//            true,
//            new Environment(),
//            keyTransform,
//            keySprite,
//            keyTriggerManager
//    );
//
//    // Trigger: Click to pick up key
//    Trigger keyClickTrigger = new Trigger(new entity.scripting.event.OnClickEvent(), true);
//
//    keyClickTrigger.addAction(new entity.scripting.action.ChangeVisibilityAction(
//            "Key",
//            new entity.scripting.expression.value.BooleanValue(false)
//    ));
//
//    entity.scripting.expression.variable.BooleanVariable hasKeyVar =
//        new entity.scripting.expression.variable.BooleanVariable("hasKey", true);
//    keyClickTrigger.addAction(
//        new entity.scripting.action.BooleanVariableAssignmentAction(
//            hasKeyVar,
//            new entity.scripting.expression.value.BooleanValue(true)
//        )
//    );
//
//    key.getTriggerManager().addTrigger(keyClickTrigger);
//    scene.addGameObject(key);
//
//    // ========== 2. CHEST 📦 ==========
//    Transform chestTransform = new Transform(
//        new Vector<>(Arrays.asList(0.0, -80.0)),
//        0f,
//        new Vector<>(Arrays.asList(0.1, 0.1))
//    );
//
//    SpriteRenderer chestSprite = new SpriteRenderer(chestImage, true);
//    TriggerManager chestTriggerManager = new TriggerManager();
//
//    GameObject chest = new GameObject(
//            "Chest",
//            "Chest",
//            true,
//            new Environment(),
//            chestTransform,
//            chestSprite,
//            chestTriggerManager
//    );
//
//    Trigger openChestTrigger = new Trigger(new entity.scripting.event.OnClickEvent(), true);
//
//    entity.scripting.expression.variable.BooleanVariable keyCheck =
//        new entity.scripting.expression.variable.BooleanVariable("hasKey", true);
//    entity.scripting.condition.BooleanComparisonCondition keyCondition =
//        new entity.scripting.condition.BooleanComparisonCondition(
//            keyCheck,
//            new entity.scripting.expression.value.BooleanValue(true)
//        );
//    openChestTrigger.addCondition(keyCondition);
//
//    openChestTrigger.addAction(new entity.scripting.action.ChangeVisibilityAction(
//            "Chest",
//            new entity.scripting.expression.value.BooleanValue(false)
//    ));
//
//    entity.scripting.expression.variable.BooleanVariable tamingSkillVar =
//        new entity.scripting.expression.variable.BooleanVariable("tamingSkillLearned", true);
//    openChestTrigger.addAction(
//        new entity.scripting.action.BooleanVariableAssignmentAction(
//            tamingSkillVar,
//            new entity.scripting.expression.value.BooleanValue(true)
//        )
//    );
//
//    entity.scripting.expression.variable.NumericVariable skillPointsVar =
//        new entity.scripting.expression.variable.NumericVariable("skillPoints", true);
//    openChestTrigger.addAction(
//        new entity.scripting.action.NumericVariableAssignmentAction(
//            skillPointsVar,
//            new entity.scripting.expression.value.NumericValue(100.0)
//        )
//    );
//
//    chest.getTriggerManager().addTrigger(openChestTrigger);
//    scene.addGameObject(chest);
//
//    // ========== 3. DRAGON 🐉 ==========
//    Transform dragonTransform = new Transform(
//        new Vector<>(Arrays.asList(180.0, 80.0)),
//        -15f,
//        new Vector<>(Arrays.asList(0.5, 0.5))
//    );
//
//    SpriteRenderer dragonSprite = new SpriteRenderer(dragonImage, true);
//    TriggerManager dragonTriggerManager = new TriggerManager();
//
//    GameObject dragon = new GameObject(
//            "Dragon",
//            "Dragon",
//            true,
//            new Environment(),
//            dragonTransform,
//            dragonSprite,
//            dragonTriggerManager
//    );
//
//    // Trigger 1: Click Dragon - TAME IT
//    Trigger tameDragonTrigger = new Trigger(new entity.scripting.event.OnClickEvent(), true);
//
//    entity.scripting.expression.variable.BooleanVariable tamingSkillCheck =
//        new entity.scripting.expression.variable.BooleanVariable("tamingSkillLearned", true);
//    entity.scripting.condition.BooleanComparisonCondition tamingCondition =
//        new entity.scripting.condition.BooleanComparisonCondition(
//            tamingSkillCheck,
//            new entity.scripting.expression.value.BooleanValue(true)
//        );
//    tameDragonTrigger.addCondition(tamingCondition);
//
//    tameDragonTrigger.addAction(new entity.scripting.action.ChangePositionAction(
//            "Dragon",
//            new entity.scripting.expression.value.NumericValue(0.0),
//            new entity.scripting.expression.value.NumericValue(0.0)
//    ));
//
//    entity.scripting.expression.variable.BooleanVariable tamedVar =
//        new entity.scripting.expression.variable.BooleanVariable("dragonTamed", true);
//    tameDragonTrigger.addAction(
//        new entity.scripting.action.BooleanVariableAssignmentAction(
//            tamedVar,
//            new entity.scripting.expression.value.BooleanValue(true)
//        )
//    );
//
//    dragon.getTriggerManager().addTrigger(tameDragonTrigger);
//
//    // Trigger 2: Press W - FLY UP
//    entity.scripting.event.OnKeyPressEvent flyUpEvent = new entity.scripting.event.OnKeyPressEvent();
//    flyUpEvent.addEventParameter("Key", "W");
//
//    Trigger flyUpTrigger = new Trigger(flyUpEvent, true);
//
//    entity.scripting.expression.variable.BooleanVariable tamedCheck =
//        new entity.scripting.expression.variable.BooleanVariable("dragonTamed", true);
//    entity.scripting.condition.BooleanComparisonCondition tamedCondition =
//        new entity.scripting.condition.BooleanComparisonCondition(
//            tamedCheck,
//            new entity.scripting.expression.value.BooleanValue(true)
//        );
//    flyUpTrigger.addCondition(tamedCondition);
//
//    flyUpTrigger.addAction(new entity.scripting.action.ChangePositionAction(
//            "Dragon",
//            new entity.scripting.expression.value.NumericValue(0.0),
//            new entity.scripting.expression.value.NumericValue(-150.0)
//    ));
//
//    dragon.getTriggerManager().addTrigger(flyUpTrigger);
//
//    // Trigger 3: Press S - FLY DOWN
//    entity.scripting.event.OnKeyPressEvent flyDownEvent = new entity.scripting.event.OnKeyPressEvent();
//    flyDownEvent.addEventParameter("Key", "S");
//
//    Trigger flyDownTrigger = new Trigger(flyDownEvent, true);
//    flyDownTrigger.addCondition(tamedCondition);
//
//    flyDownTrigger.addAction(new entity.scripting.action.ChangePositionAction(
//            "Dragon",
//            new entity.scripting.expression.value.NumericValue(0.0),
//            new entity.scripting.expression.value.NumericValue(80.0)
//    ));
//
//    dragon.getTriggerManager().addTrigger(flyDownTrigger);
//
//    // Trigger 4: Press A - FLY LEFT (advanced)
//    entity.scripting.event.OnKeyPressEvent flyLeftEvent = new entity.scripting.event.OnKeyPressEvent();
//    flyLeftEvent.addEventParameter("Key", "A");
//
//    Trigger flyLeftTrigger = new Trigger(flyLeftEvent, true);
//    flyLeftTrigger.addCondition(tamedCondition);
//
//    entity.scripting.expression.variable.NumericVariable skillCheck =
//        new entity.scripting.expression.variable.NumericVariable("skillPoints", true);
//    entity.scripting.condition.NumericComparisonCondition skillCondition =
//        new entity.scripting.condition.NumericComparisonCondition(
//            skillCheck,
//            ">",
//            new entity.scripting.expression.value.NumericValue(50.0)
//        );
//    flyLeftTrigger.addCondition(skillCondition);
//
//    flyLeftTrigger.addAction(new entity.scripting.action.ChangePositionAction(
//            "Dragon",
//            new entity.scripting.expression.value.NumericValue(-200.0),
//            new entity.scripting.expression.value.NumericValue(0.0)
//    ));
//
//    dragon.getTriggerManager().addTrigger(flyLeftTrigger);
//
//    // Trigger 5: Press D - RELEASE
//    entity.scripting.event.OnKeyPressEvent releaseEvent = new entity.scripting.event.OnKeyPressEvent();
//    releaseEvent.addEventParameter("Key", "D");
//
//    Trigger releaseTrigger = new Trigger(releaseEvent, true);
//    releaseTrigger.addCondition(tamedCondition);
//
//    releaseTrigger.addAction(new entity.scripting.action.ChangePositionAction(
//            "Dragon",
//            new entity.scripting.expression.value.NumericValue(0.0),
//            new entity.scripting.expression.value.NumericValue(-250.0)
//    ));
//
//    releaseTrigger.addAction(new entity.scripting.action.ChangeVisibilityAction(
//            "Dragon",
//            new entity.scripting.expression.value.BooleanValue(false)
//    ));
//
//    dragon.getTriggerManager().addTrigger(releaseTrigger);
//
//    // Trigger 6: Press R - RESET
//    entity.scripting.event.OnKeyPressEvent resetEvent = new entity.scripting.event.OnKeyPressEvent();
//    resetEvent.addEventParameter("Key", "R");
//
//    Trigger resetTrigger = new Trigger(resetEvent, true);
//
//    resetTrigger.addAction(new entity.scripting.action.ChangePositionAction(
//            "Dragon",
//            new entity.scripting.expression.value.NumericValue(180.0),
//            new entity.scripting.expression.value.NumericValue(80.0)
//    ));
//
//    resetTrigger.addAction(new entity.scripting.action.ChangeVisibilityAction(
//            "Dragon",
//            new entity.scripting.expression.value.BooleanValue(true)
//    ));
//
//    dragon.getTriggerManager().addTrigger(resetTrigger);
//
//    scene.addGameObject(dragon);
//
//    // Print guide
//    System.out.println("\n╔════════════════════════════════════════════════════════════╗");
//    System.out.println("║           🐉 DRAGON TAMING ADVENTURE 🐉                   ║");
//    System.out.println("╚════════════════════════════════════════════════════════════╝");
//    System.out.println("\n🎮 QUEST: Tame the Wild Dragon!");
//    System.out.println("\n📍 STEP 1: Click Key 🔑 → Pick it up");
//    System.out.println("📍 STEP 2: Click Chest 📦 → Learn taming skill (requires key)");
//    System.out.println("📍 STEP 3: Click Dragon 🐉 → Tame it (requires skill)");
//    System.out.println("📍 STEP 4: Press W/S/A → Control dragon");
//    System.out.println("📍 STEP 5: Press D → Release dragon\n");
//
//    return scene;
//}
/**
     * Load an image for demo scene
     * ADDED BY CHENG for Use Case 5: Preview/Testing Feature
     */
    private entity.Image loadDemoImage(String path) {
        try {
            java.nio.file.Path imagePath = java.nio.file.Paths.get(path);

            // Check if file exists
            if (!imagePath.toFile().exists()) {
                System.err.println("❌ Image not found: " + path);
                return null;
            }

            // Use Image constructor that takes Path
            entity.Image image = new entity.Image(imagePath);
            System.out.println("✓ Loaded image: " + path);
            return image;

        } catch (Exception e) {
            System.err.println("❌ Failed to load image: " + path);
            e.printStackTrace();
            return null;
        }
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
        java.awt.EventQueue.invokeLater(() -> {
            entity.AssetLib assetLib = new entity.AssetLib();

            interface_adapter.assets.AssetLibViewModel assetLibViewModel =
                    new interface_adapter.assets.AssetLibViewModel(assetLib);
            interface_adapter.sprites.ImportSpriteViewModel importSpriteViewModel =
                    new interface_adapter.sprites.ImportSpriteViewModel();
            interface_adapter.sprites.ImportSpriteFromUnsplashViewModel unsplashViewModel =
                    new interface_adapter.sprites.ImportSpriteFromUnsplashViewModel();

            app.use_case_factory.SpriteImportUseCaseFactory.loadExistingAssets(assetLib);

            interface_adapter.sprites.ImportSpriteController importSpriteController =
                    app.use_case_factory.SpriteImportUseCaseFactory.createLocalImportUseCase(
                            assetLibViewModel, importSpriteViewModel);

            String unsplashApiKey = System.getenv("UNSPLASH_ACCESS_KEY");
            interface_adapter.sprites.ImportSpriteFromUnsplashController unsplashController =
                    app.use_case_factory.SpriteImportUseCaseFactory.createUnsplashImportUseCase(
                            assetLibViewModel, unsplashViewModel, unsplashApiKey);

            HomeView view = new HomeView(
                    assetLibViewModel,
                    importSpriteController,
                    importSpriteViewModel,
                    unsplashController,
                    unsplashViewModel
            );
            view.setVisible(true);
        });
    }
}
