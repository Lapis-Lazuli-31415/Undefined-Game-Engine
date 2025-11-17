package app;

import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * InputManager - Handles keyboard and mouse input
 *
 * Features:
 * - Tracks currently pressed keys
 * - Tracks mouse clicks and position
 * - Provides query methods for input state
 *
 * @author Wanru Cheng
 */
public class InputManager {

    // Keyboard state
    private HashSet<String> pressedKeys;
    private HashSet<String> justPressedKeys;

    // Mouse state
    private Point mousePosition;
    private Point lastClickPosition;
    private boolean mousePressed;

    // Listeners
    private KeyListener keyListener;
    private MouseListener mouseListener;
    private MouseMotionListener mouseMotionListener;

    /**
     * Create a new InputManager
     */
    public InputManager() {
        this.pressedKeys = new HashSet<>();
        this.justPressedKeys = new HashSet<>();
        this.mousePosition = new Point(0, 0);
        this.lastClickPosition = null;
        this.mousePressed = false;

        setupListeners();
    }

    /**
     * Setup input listeners
     */
    private void setupListeners() {
        // Keyboard listener
        keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String key = KeyEvent.getKeyText(e.getKeyCode());

                // Only add to justPressed if not already pressed
                if (!pressedKeys.contains(key)) {
                    justPressedKeys.add(key);
                }

                pressedKeys.add(key);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String key = KeyEvent.getKeyText(e.getKeyCode());
                pressedKeys.remove(key);
                justPressedKeys.remove(key);
            }
        };

        // Mouse click listener
        mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePressed = true;
                lastClickPosition = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePressed = false;
            }
        };

        // Mouse motion listener
        mouseMotionListener = new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePosition = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                mousePosition = e.getPoint();
            }
        };
    }

    /**
     * Update input state (call once per frame)
     */
    public void update() {
        // Clear one-time events
        justPressedKeys.clear();
        lastClickPosition = null;
    }

    /**
     * Check if a key is currently pressed
     *
     * @param key Key name (e.g., "W", "Space", "Up")
     * @return true if key is pressed
     */
    public boolean isKeyPressed(String key) {
        return pressedKeys.contains(key);
    }

    /**
     * Check if a key was just pressed this frame
     *
     * @param key Key name
     * @return true if key was just pressed
     */
    public boolean isKeyJustPressed(String key) {
        return justPressedKeys.contains(key);
    }

    /**
     * Get all currently pressed keys
     *
     * @return List of pressed key names
     */
    public ArrayList<String> getPressedKeys() {
        return new ArrayList<>(pressedKeys);
    }

    /**
     * Get keys that were just pressed this frame
     *
     * @return List of just pressed key names
     */
    public ArrayList<String> getJustPressedKeys() {
        return new ArrayList<>(justPressedKeys);
    }

    /**
     * Get current mouse position
     *
     * @return Mouse position
     */
    public Point getMousePosition() {
        return new Point(mousePosition);
    }

    /**
     * Get last click position (null if no click this frame)
     *
     * @return Click position or null
     */
    public Point getLastClickPosition() {
        return lastClickPosition != null ? new Point(lastClickPosition) : null;
    }

    /**
     * Check if mouse is currently pressed
     *
     * @return true if mouse button is down
     */
    public boolean isMousePressed() {
        return mousePressed;
    }

    /**
     * Get the KeyListener for attaching to components
     *
     * @return KeyListener
     */
    public KeyListener getKeyListener() {
        return keyListener;
    }

    /**
     * Get the MouseListener for attaching to components
     *
     * @return MouseListener
     */
    public MouseListener getMouseListener() {
        return mouseListener;
    }

    /**
     * Get the MouseMotionListener for attaching to components
     *
     * @return MouseMotionListener
     */
    public MouseMotionListener getMouseMotionListener() {
        return mouseMotionListener;
    }
}