package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.Vector;
import javax.swing.*;

import entity.*;
import entity.scripting.TriggerManager;
import entity.scripting.environment.Environment;
import interface_adapter.transform.TransformViewModel;
import interface_adapter.transform.TransformController;
import app.TransformUseCaseFactory;
import app.VariableUseCaseFactory;
import interface_adapter.variable.DeleteVariableController;
import interface_adapter.variable.UpdateVariableController;
import interface_adapter.variable.GlobalVariableViewModel;
import interface_adapter.variable.LocalVariableViewModel;
import view.property.PropertiesPanel;

import data_access.saving.JsonProjectDataAccess;
import use_case.saving.SaveProjectInteractor;
import interface_adapter.saving.SaveProjectController;
import interface_adapter.saving.SaveProjectPresenter;
import interface_adapter.saving.SaveProjectViewModel;
import interface_adapter.saving.SaveProjectState;

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

    // variable management
    private GlobalVariableViewModel globalVariableViewModel;
    private LocalVariableViewModel localVariableViewModel;
    private UpdateVariableController updateVariableController;
    private DeleteVariableController deleteVariableController;

    // Demo wiring
    private ScenePanel scenePanel;
    private static GameObject DEMO_OBJECT;
    private TransformViewModel transformViewModel;
    private TransformController transformController;

    public static GameObject getDemoGameObject() {
        return DEMO_OBJECT;
    }

    public HomeView(
            interface_adapter.assets.AssetLibViewModel assetLibViewModel,
            interface_adapter.sprites.ImportSpriteController importSpriteController,
            interface_adapter.sprites.ImportSpriteViewModel importSpriteViewModel,
            interface_adapter.sprites.ImportSpriteFromUnsplashController unsplashController,
            interface_adapter.sprites.ImportSpriteFromUnsplashViewModel unsplashViewModel) {

        this.assetLibViewModel = assetLibViewModel;
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

        initComponents();
        setupAssetLibListener();
        setupImportSpriteListener();
    }

    private GameObject createDefaultGameObject() {
        return new GameObject(
                "demo-1", "Demo Sprite", true, new ArrayList<>(), null,
                new Transform(new Vector<>(Arrays.asList(0.0, 0.0)), 0f, new Vector<>(Arrays.asList(1.0, 1.0))),
                new TriggerManager()
        );
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
        menuBar.add(new JMenu("Scene"));
        menuBar.add(new JMenu("Debug"));
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

        spritesAddButton = new JButton("+");
        spritesAddButton.setMargin(new Insets(0, 4, 0, 4));
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
        saveStatusLabel.setFont(new Font("Arial", Font.BOLD, 12));
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

        rightTabControls.add(playButton);
        rightTabControls.add(stopButton);

        tabBar.add(tabLabel, BorderLayout.WEST);
        tabBar.add(rightTabControls, BorderLayout.EAST);

        // ====== RIGHT PROPERTIES PANEL ======
        propertiesPanel = new PropertiesPanel();

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

        scenePanel = new ScenePanel(transformViewModel);
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
            props.bind(
                    transformViewModel,
                    transformController,
                    () -> {
                        scenePanel.repaint();
                        triggerAutoSave();
                    }
            );
            props.setAutoSaveCallback(() -> triggerAutoSave());
        }

        // Selection Wiring
        scenePanel.setOnSelectionChangeCallback(() -> {
            GameObject selectedObject = scenePanel.getSelectedObject();
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
                VariableUseCaseFactory.create(globalEnvironment, localEnvironment);

        globalVariableViewModel = variableWiring.getGlobalViewModel();
        localVariableViewModel = variableWiring.getLocalViewModel();
        updateVariableController = variableWiring.getUpdateController();
        deleteVariableController = variableWiring.getDeleteController();

        if (propertiesPanel instanceof PropertiesPanel props) {
            props.setLocalVariableViewModel(localVariableViewModel);
            props.setGlobalVariableViewModel(globalVariableViewModel);
            props.setVariableController(updateVariableController);
            props.setDeleteVariableController(deleteVariableController);
        }

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