package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.*;

import entity.GameController;
import entity.GameObject;
import entity.Transform;
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
import use_case.sprites.ImportSpriteInteractor;
import view.property.PropertiesPanel;

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
    private final Environment globalEnvironment = new Environment();

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

    // TODO: Delete this after gameObject selection is implemented
    public static GameObject getDemoGameObject() {
        return DEMO_OBJECT;
    }

    /**
     * Constructor that accepts all dependencies (for production use)
     */
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

        // init unsplash view if controller is available
        if (unsplashController != null && unsplashViewModel != null) {
            this.unsplashView = new ImportSpriteFromUnsplashView(unsplashViewModel, unsplashController);
        }

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
            importSpriteViewModel = new interface_adapter.sprites.ImportSpriteViewModel();

            // create presenter
            interface_adapter.sprites.ImportSpritePresenter presenter =
                    new interface_adapter.sprites.ImportSpritePresenter(importSpriteViewModel, assetLibViewModel);

            // create interactor
            ImportSpriteInteractor interactor =
                    new ImportSpriteInteractor(
                            spriteDAO,
                            presenter,
                            assetLibViewModel.getAssetLib()
                    );

            // create controller
            importSpriteController = new interface_adapter.sprites.ImportSpriteController(interactor);

        } catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to initialize sprite import: " + e.getMessage(),
                    "Initialization Error",
                    JOptionPane.ERROR_MESSAGE);
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

        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel homeTabContent = new JPanel(new BorderLayout());

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
        spritesAddButton.addActionListener(e -> openSpriteImportMenu());

        spritesHeader.add(spritesLabel, BorderLayout.WEST);
        spritesHeader.add(spritesAddButton, BorderLayout.EAST);

        // gallery grid view sprites
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

        // update properties panel when selection changes
        scenePanel.setOnSelectionChangeCallback(() -> {
            GameObject selectedObject = scenePanel.getSelectedObject();
            if (propertiesPanel instanceof PropertiesPanel props && selectedObject != null) {
                props.bindSpriteRenderer(selectedObject, assetLibViewModel, () -> scenePanel.repaint());
            }
        });
        // variable usecase wiring
        Environment localEnvironment = new Environment();
        DEMO_OBJECT.setEnvironment(localEnvironment);

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

        // Add all components to home tab content
        homeTabContent.add(leftSidebar, BorderLayout.WEST);
        homeTabContent.add(centerPanel, BorderLayout.CENTER);
        homeTabContent.add(propertiesScroll, BorderLayout.EAST);

        // Add home tab to tabbed pane
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

        // Show menu below the + button
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

    public interface_adapter.assets.AssetLibViewModel getAssetLibViewModel() {
        return assetLibViewModel;
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