package view;

import entity.Scene;
import entity.scripting.environment.Environment;

import javax.swing.*;
import java.awt.*;

/**
 * PreviewWindow - Window for previewing and testing the game
 *
 * @author Wanru Cheng
 */
public class PreviewWindow {

    private JFrame frame;
    private Scene scene;
    private GameCanvas canvas;
    private RuntimeEnvironment runtime;
    private Environment globalEnvironment;

    private JButton playButton;
    private JButton stopButton;
    private JLabel statusLabel;

    /**
     * Create a PreviewWindow
     *
     * @param scene The scene to preview
     * @param globalEnvironment The global environment for triggers
     */
    public PreviewWindow(Scene scene, Environment globalEnvironment) {
        this.scene = scene;
        this.globalEnvironment = globalEnvironment;

        initializeUI();
    }

    /**
     * Initialize the UI
     */
    private void initializeUI() {
        frame = new JFrame("Preview - " + scene.getName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLayout(new BorderLayout());

        // Create canvas
        canvas = new GameCanvas();
        canvas.setGameObjects(scene.getGameObjects());

        // Create control panel
        JPanel controlPanel = createControlPanel();

        // Add components
        frame.add(canvas, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Setup runtime
        setupRuntime();
    }

    /**
     * Create control panel with play/stop buttons
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(60, 60, 60));
        panel.setPreferredSize(new Dimension(900, 50));

        // Play button
        playButton = new JButton("▶ Play");
        playButton.addActionListener(e -> startPreview());

        // Stop button
        stopButton = new JButton("⏹ Stop");
        stopButton.setEnabled(false);
        stopButton.addActionListener(e -> stopPreview());

        // Status label
        statusLabel = new JLabel("Ready");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        panel.add(playButton);
        panel.add(stopButton);
        panel.add(statusLabel);

        return panel;
    }

    /**
     * Setup RuntimeEnvironment
     */
    private void setupRuntime() {
        // ✅ Pass 3 arguments: scene, canvas, globalEnvironment
        runtime = new RuntimeEnvironment(scene, canvas, globalEnvironment);
    }

    /**
     * Start the preview
     */
    private void startPreview() {
        runtime.start();

        playButton.setEnabled(false);
        stopButton.setEnabled(true);
        statusLabel.setText("Running...");
        statusLabel.setForeground(Color.GREEN);

        System.out.println("Preview started");
    }

    /**
     * Stop the preview
     */
    private void stopPreview() {
        runtime.stop();

        playButton.setEnabled(true);
        stopButton.setEnabled(false);
        statusLabel.setText("Stopped");
        statusLabel.setForeground(Color.RED);

        System.out.println("Preview stopped");
    }

    /**
     * Display the preview window
     */
    public void display() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Request focus for keyboard input
        canvas.requestFocus();
    }

    /**
     * Close the preview window
     */
    public void close() {
        if (runtime != null && runtime.isRunning()) {
            runtime.stop();
        }

        if (canvas != null) {
            canvas.dispose();
        }

        if (frame != null) {
            frame.dispose();
        }
    }

    /**
     * Check if preview is running
     */
    public boolean isRunning() {
        return runtime != null && runtime.isRunning();
    }
}