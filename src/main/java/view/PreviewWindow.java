package view;

import entity.Scene;
import entity.GameObject;
import entity.scripting.environment.Environment;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.InputManager;
import interface_adapter.preview.EventListenerFactory;

import javax.swing.*;
import java.awt.*;

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
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(60, 60, 60));
        panel.setPreferredSize(new Dimension(900, 40));

        statusLabel = new JLabel("Running...");
        statusLabel.setForeground(Color.GREEN);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel instructionLabel = new JLabel("  |  Press keys or click objects to test triggers");
        instructionLabel.setForeground(Color.LIGHT_GRAY);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        panel.add(statusLabel);
        panel.add(instructionLabel);

        return panel;
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
        canvas.setGameObjects(scene.getGameObjects());

        // Count triggers
        int triggerCount = countTotalTriggers();
        System.out.println("Found " + triggerCount + " triggers");

        // Check for background music
        if (scene.getBackgroundMusic() != null) {
            System.out.println("Background music: " + scene.getBackgroundMusic());
        } else {
            System.out.println("No background music");
        }

        System.out.println("=== Scene Loaded ===\n");
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

        // Request focus for keyboard input
        canvas.requestFocus();

        // Auto-start the game loop
        runtime.start();

        System.out.println("Preview window displayed and started");
        System.out.println("Canvas has focus: " + canvas.hasFocus());
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