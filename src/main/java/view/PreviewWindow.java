package view;

import entity.Scene;
import entity.GameObject;
import entity.scripting.environment.Environment;
import entity.scripting.TriggerManager;
import interface_adapter.preview.EventListenerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * PreviewWindow - Auto-starts when opened via Play button.
 * Part of View layer (blue ring in CA diagram).
 *
 * Responsibilities:
 * - Display preview window UI
 * - Create and coordinate GameCanvas and RuntimeEnvironment
 * - Show scene information
 * - Handle window lifecycle
 *
 * OPTIMIZED: Now creates EventListenerFactory and passes to components
 * following dependency injection pattern.
 *
 * @author Wanru Cheng
 */
public class PreviewWindow {

    private JFrame frame;
    private Scene scene;
    private GameCanvas canvas;
    private RuntimeEnvironment runtime;
    private Environment globalEnvironment;
    private EventListenerFactory listenerFactory;
    private InputManager inputManager;

    private JLabel statusLabel;
    private JLabel sceneInfoLabel;
    private JPanel objectButtonsPanel;  // Panel for object buttons in button mode
    /**
     * Create a PreviewWindow.
     *
     * @param scene The scene to preview
     * @param globalEnvironment The global environment for triggers
     */
    public PreviewWindow(Scene scene, Environment globalEnvironment) {
        this.scene = scene;
        this.globalEnvironment = globalEnvironment;

        // Create InputManager and Factory (Interface Adapter layer)
        this.inputManager = new InputManager();
        this.listenerFactory = new EventListenerFactory(inputManager);

        initializeUI();
        loadScene();
    }

    /**
     * Initialize the UI components.
     */
    private void initializeUI() {
        frame = new JFrame("Preview - " + scene.getName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLayout(new BorderLayout());

        // Add window listener to stop runtime when closed
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                close();
            }
        });

        // Create canvas
        canvas = new GameCanvas();
        canvas.setListenerFactory(listenerFactory); // Inject factory into canvas
        // ✅ ADD THIS CODE RIGHT HERE (after canvas is created)
        canvas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!canvas.hasFocus()) {
                    canvas.requestFocusInWindow();
                    System.out.println("Canvas focused via mouse click");
                }
            }
        });
        // Create info panel
        JPanel infoPanel = createInfoPanel();

        // Create status panel
        JPanel statusPanel = createStatusPanel();

        // Add components
        frame.add(infoPanel, BorderLayout.NORTH);
        frame.add(canvas, BorderLayout.CENTER);
        frame.add(statusPanel, BorderLayout.SOUTH);

        // Setup runtime
        setupRuntime();
    }
    /**
     * Create info panel showing scene information.
     *
     * @return The info panel
     */
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(50, 50, 50));
        panel.setPreferredSize(new Dimension(900, 40));

        sceneInfoLabel = new JLabel();
        sceneInfoLabel.setForeground(Color.WHITE);
        sceneInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        updateSceneInfo();

        panel.add(sceneInfoLabel);

        return panel;
    }

    /**
     * Create status panel at bottom.
     *
     * @return The status panel
     */
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(60, 60, 60));
        panel.setPreferredSize(new Dimension(900, 70));

        // Top row: status and mode toggle
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topRow.setBackground(new Color(60, 60, 60));

        statusLabel = new JLabel("Running...");
        statusLabel.setForeground(Color.GREEN);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel instructionLabel = new JLabel("  |  Press keys or click objects to test triggers");
        instructionLabel.setForeground(Color.LIGHT_GRAY);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton toggleModeButton = new JButton("Mode: Collision");
        toggleModeButton.addActionListener(e -> {
            boolean newMode = !canvas.isUsingButtonMode();

            if (runtime != null && runtime.isRunning()) {
                runtime.stop();
            }

            canvas.setUseButtonMode(newMode);
            canvas.setGameObjects(new ArrayList<>(scene.getGameObjects()));
            // Update object buttons panel
            updateObjectButtonsPanel();

            runtime.start();

            if (newMode) {
                toggleModeButton.setText("Mode: Button");
            } else {
                toggleModeButton.setText("Mode: Collision");
            }

            System.out.println("Switched to: " + (newMode ? "Button Mode" : "Collision Mode"));
        });

        topRow.add(statusLabel);
        topRow.add(instructionLabel);
        topRow.add(toggleModeButton);

        // Bottom row: object buttons (for button mode)
        objectButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        objectButtonsPanel.setBackground(new Color(50, 50, 50));

        panel.add(topRow, BorderLayout.NORTH);
        panel.add(objectButtonsPanel, BorderLayout.CENTER);

        return panel;
    }
    /**
     * Update the object buttons panel based on current mode.
     * Shows clickable object buttons only in Button Mode.
     */
    private void updateObjectButtonsPanel() {
        objectButtonsPanel.removeAll();

        if (canvas.isUsingButtonMode()) {
            // Add a button for each object with OnClickEvent
            for (GameObject obj : scene.getGameObjects()) {
                if (!obj.isActive()) continue;

                if (hasOnClickEvent(obj)) {
                    JButton objButton = new JButton(obj.getName());
                    objButton.addActionListener(e -> {
                        // Get the click listener and trigger it
                        entity.event_listener.ClickListener listener = canvas.getClickListener(obj);
                        if (listener != null) {
                            listener.notifyClicked();
                            System.out.println("Button clicked: " + obj.getName());
                        }
                    });
                    objectButtonsPanel.add(objButton);
                }
            }

            if (objectButtonsPanel.getComponentCount() == 0) {
                JLabel noObjectsLabel = new JLabel("No clickable objects");
                noObjectsLabel.setForeground(Color.GRAY);
                objectButtonsPanel.add(noObjectsLabel);
            }
        } else {
            JLabel collisionLabel = new JLabel("Click objects in the scene above");
            collisionLabel.setForeground(Color.GRAY);
            objectButtonsPanel.add(collisionLabel);
        }

        objectButtonsPanel.revalidate();
        objectButtonsPanel.repaint();
    }

    /**
     * Check if GameObject has OnClickEvent trigger.
     */
    private boolean hasOnClickEvent(GameObject obj) {
        entity.scripting.TriggerManager tm = obj.getTriggerManager();
        if (tm == null) return false;

        for (entity.scripting.Trigger trigger : tm.getAllTriggers()) {
            if (trigger.getEvent() instanceof entity.scripting.event.OnClickEvent) {
                return true;
            }
        }
        return false;
    }
    /**
     * Update scene info display.
     */
    private void updateSceneInfo() {
        int objectCount = scene.getGameObjects().size();
        int activeCount = (int) scene.getGameObjects().stream()
                .filter(GameObject::isActive).count();
        int triggerCount = countTotalTriggers();

        String info = String.format(
                "Scene: %s | Objects: %d (%d active) | Triggers: %d",
                scene.getName(), objectCount, activeCount, triggerCount
        );

        sceneInfoLabel.setText(info);
    }

    /**
     * Count total triggers in scene.
     *
     * @return Total number of triggers
     */
    private int countTotalTriggers() {
        int count = 0;
        for (GameObject obj : scene.getGameObjects()) {
            TriggerManager tm = obj.getTriggerManager();
            if (tm != null) {
                count += tm.getAllTriggers().size();
            }
        }
        return count;
    }

    /**
     * Load scene into preview.
     */
    private void loadScene() {
        System.out.println("\n=== Loading Scene: " + scene.getName() + " ===");

        // Load GameObjects
        System.out.println("Loading " + scene.getGameObjects().size() + " GameObjects...");
        canvas.setGameObjects(new ArrayList<>(scene.getGameObjects()));

        // Count triggers
        int triggerCount = countTotalTriggers();
        System.out.println("Found " + triggerCount + " triggers");

//        // Check for background music
//        if (scene.getBackgroundMusic() != null) {
//            System.out.println("Background music: " + scene.getBackgroundMusic());
//        } else {
//            System.out.println("No background music");
//        }
//        // Update object buttons panel
//        updateObjectButtonsPanel();
//        System.out.println("=== Scene Loaded ===\n");
    }

    /**
     * Setup RuntimeEnvironment.
     * OPTIMIZED: Injects EventListenerFactory into RuntimeEnvironment.
     */
    private void setupRuntime() {
        // Create RuntimeEnvironment with factory
        runtime = new RuntimeEnvironment(
                scene,
                canvas,
                globalEnvironment,
                listenerFactory  // Inject factory
        );
    }

    /**
     * Display the preview window and auto-start.
     */
    public void display() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Multiple attempts to grab focus
        canvas.setFocusable(true);
        canvas.requestFocus();

        SwingUtilities.invokeLater(() -> {
            canvas.requestFocusInWindow();
            frame.toFront();
            frame.requestFocus();
            canvas.requestFocusInWindow();

            System.out.println("Canvas focus after delay: " + canvas.hasFocus());

            // If still no focus, print instructions
            if (!canvas.hasFocus()) {
                System.out.println("⚠️  Canvas doesn't have focus yet!");
                System.out.println("   Click on the canvas window to enable keyboard input!");
            }
        });

        // Auto-start the game loop
        runtime.start();

        System.out.println("Preview window displayed and started");
        System.out.println("\nTry pressing keys or clicking objects!");
        System.out.println("Watch console for trigger outputs\n");
    }

    /**
     * Close the preview window and stop runtime.
     */
    public void close() {
        if (runtime != null && runtime.isRunning()) {
            runtime.stop();
            System.out.println("Runtime stopped");
        }

        if (canvas != null) {
            canvas.dispose();
        }

        if (frame != null) {
            frame.dispose();
        }

        System.out.println("Preview window closed\n");
    }

    /**
     * Check if preview is running.
     *
     * @return true if running
     */
    public boolean isRunning() {
        return runtime != null && runtime.isRunning();
    }
}