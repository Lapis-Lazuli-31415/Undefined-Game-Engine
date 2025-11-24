//package entity;
//
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * InputManager - Manages keyboard and mouse input state.
// * Part of Entity layer - contains input state business rules.
// *
// * Features:
// * - Keyboard: pressed keys, just pressed keys
// * - Mouse: click position, button state
// * - Frame-based state management
// *
// * ENHANCED: Now supports mouse input for click detection
// *
// * @author Wanru Cheng
// */
//public class InputManager extends KeyAdapter {
//
//    // ===== Keyboard State =====
//    private final Set<String> pressedKeys;
//    private final Set<String> justPressedKeys;
//
//    // ===== Mouse State =====
//    private int mouseX;
//    private int mouseY;
//    private boolean mouseClicked;
//    private boolean mouseJustClicked;
//    private int mouseButton;  // 1=left, 2=middle, 3=right
//
//    /**
//     * Constructor.
//     */
//    public InputManager() {
//        this.pressedKeys = new HashSet<>();
//        this.justPressedKeys = new HashSet<>();
//        this.mouseX = 0;
//        this.mouseY = 0;
//        this.mouseClicked = false;
//        this.mouseJustClicked = false;
//        this.mouseButton = 0;
//    }
//
//    // ========== KEYBOARD METHODS ==========
//
//    /**
//     * Handle key pressed event.
//     *
//     * @param e Key event
//     */
//    @Override
//    public void keyPressed(KeyEvent e) {
//        String key = getKeyText(e);
//
//        // Add to pressed keys
//        if (!pressedKeys.contains(key)) {
//            pressedKeys.add(key);
//            justPressedKeys.add(key);
//        }
//    }
//
//    /**
//     * Handle key released event.
//     *
//     * @param e Key event
//     */
//    @Override
//    public void keyReleased(KeyEvent e) {
//        String key = getKeyText(e);
//        pressedKeys.remove(key);
//    }
//
//    /**
//     * Check if a key is currently pressed.
//     *
//     * @param key Key name (case-insensitive)
//     * @return true if pressed
//     */
//    public boolean isKeyPressed(String key) {
//        return pressedKeys.contains(key.toUpperCase());
//    }
//
//    /**
//     * Check if a key was just pressed this frame.
//     *
//     * @param key Key name (case-insensitive)
//     * @return true if just pressed
//     */
//    public boolean isKeyJustPressed(String key) {
//        return justPressedKeys.contains(key.toUpperCase());
//    }
//
//    /**
//     * Get normalized key text from KeyEvent.
//     *
//     * @param e Key event
//     * @return Normalized key name
//     */
//    private String getKeyText(KeyEvent e) {
//        int keyCode = e.getKeyCode();
//        String keyText = KeyEvent.getKeyText(keyCode);
//        return keyText.toUpperCase();
//    }
//
//    // ========== MOUSE METHODS ==========
//
//    /**
//     * Get mouse listener for attaching to canvas.
//     * This allows InputManager to receive mouse events.
//     *
//     * @return MouseAdapter that updates mouse state
//     */
//    public MouseAdapter getMouseListener() {
//        return new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                handleMousePressed(e);
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                handleMouseReleased(e);
//            }
//
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                handleMouseMoved(e);
//            }
//
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                handleMouseMoved(e);
//            }
//        };
//    }
//
//    /**
//     * Handle mouse pressed event.
//     *
//     * @param e Mouse event
//     */
//    private void handleMousePressed(MouseEvent e) {
//        mouseX = e.getX();
//        mouseY = e.getY();
//        mouseButton = e.getButton();
//
//        if (!mouseClicked) {
//            mouseClicked = true;
//            mouseJustClicked = true;
//        }
//    }
//
//    /**
//     * Handle mouse released event.
//     *
//     * @param e Mouse event
//     */
//    private void handleMouseReleased(MouseEvent e) {
//        mouseClicked = false;
//        mouseButton = 0;
//    }
//
//    /**
//     * Handle mouse moved event.
//     *
//     * @param e Mouse event
//     */
//    private void handleMouseMoved(MouseEvent e) {
//        mouseX = e.getX();
//        mouseY = e.getY();
//    }
//
//    /**
//     * Get current mouse X position.
//     *
//     * @return Mouse X coordinate
//     */
//    public int getMouseX() {
//        return mouseX;
//    }
//
//    /**
//     * Get current mouse Y position.
//     *
//     * @return Mouse Y coordinate
//     */
//    public int getMouseY() {
//        return mouseY;
//    }
//
//    /**
//     * Check if mouse is currently clicked.
//     *
//     * @return true if mouse button is down
//     */
//    public boolean isMouseClicked() {
//        return mouseClicked;
//    }
//
//    /**
//     * Check if mouse was just clicked this frame.
//     *
//     * @return true if just clicked
//     */
//    public boolean isMouseJustClicked() {
//        return mouseJustClicked;
//    }
//
//    /**
//     * Get which mouse button was clicked.
//     *
//     * @return 1=left, 2=middle, 3=right, 0=none
//     */
//    public int getMouseButton() {
//        return mouseButton;
//    }
//
//    /**
//     * Check if left mouse button was just clicked.
//     *
//     * @return true if left button just clicked
//     */
//    public boolean isLeftClickJustPressed() {
//        return mouseJustClicked && mouseButton == MouseEvent.BUTTON1;
//    }
//
//    /**
//     * Check if right mouse button was just clicked.
//     *
//     * @return true if right button just clicked
//     */
//    public boolean isRightClickJustPressed() {
//        return mouseJustClicked && mouseButton == MouseEvent.BUTTON3;
//    }
//
//    // ========== LIFECYCLE METHODS ==========
//
//    /**
//     * Update input state (call once per frame).
//     * Clears "just pressed" states.
//     */
//    public void update() {
//        // Clear just pressed keys
//        justPressedKeys.clear();
//
//        // Clear just clicked state
//        mouseJustClicked = false;
//    }
//
//    /**
//     * Reset all input state.
//     */
//    public void reset() {
//        pressedKeys.clear();
//        justPressedKeys.clear();
//        mouseX = 0;
//        mouseY = 0;
//        mouseClicked = false;
//        mouseJustClicked = false;
//        mouseButton = 0;
//    }
//
//    // ========== DEBUG METHODS ==========
//
//    /**
//     * Get all currently pressed keys.
//     *
//     * @return Set of pressed key names
//     */
//    public Set<String> getPressedKeys() {
//        return new HashSet<>(pressedKeys);
//    }
//
//    /**
//     * Get current mouse state as string.
//     *
//     * @return Mouse state description
//     */
//    public String getMouseStateString() {
//        return String.format("Mouse[x=%d, y=%d, clicked=%b, button=%d]",
//                mouseX, mouseY, mouseClicked, mouseButton);
//    }
//}
package entity;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class InputManager extends KeyAdapter {

    // ===== Keyboard State =====
    private final Set<String> pressedKeys;
    private final Set<String> justPressedKeys;

    // ===== Mouse State =====
    private int mouseX;
    private int mouseY;
    private boolean mouseClicked;
    private boolean mouseJustClicked;
    private int mouseButton;

    // ===== 新增：保存 MouseAdapter 实例 =====
    private final MouseAdapter mouseAdapter;

    public InputManager() {
        this.pressedKeys = new HashSet<>();
        this.justPressedKeys = new HashSet<>();
        this.mouseX = 0;
        this.mouseY = 0;
        this.mouseClicked = false;
        this.mouseJustClicked = false;
        this.mouseButton = 0;

        // 在构造函数中创建 MouseAdapter，只创建一次
        this.mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseReleased(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseMoved(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseMoved(e);
            }
        };
    }

    // ========== KEYBOARD METHODS ==========

    @Override
    public void keyPressed(KeyEvent e) {
        String key = getKeyText(e);
        if (!pressedKeys.contains(key)) {
            pressedKeys.add(key);
            justPressedKeys.add(key);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        String key = getKeyText(e);
        pressedKeys.remove(key);
    }

    public boolean isKeyPressed(String key) {
        return pressedKeys.contains(key.toUpperCase());
    }

    public boolean isKeyJustPressed(String key) {
        return justPressedKeys.contains(key.toUpperCase());
    }

    private String getKeyText(KeyEvent e) {
        int keyCode = e.getKeyCode();
        String keyText = KeyEvent.getKeyText(keyCode);
        return keyText.toUpperCase();
    }

    // ========== MOUSE METHODS ==========

    /**
     * 返回同一个 MouseAdapter 实例
     */
    public MouseAdapter getMouseListener() {
        return mouseAdapter;
    }

    private void handleMousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseButton = e.getButton();

        if (!mouseClicked) {
            mouseClicked = true;
            mouseJustClicked = true;
        }
    }

    private void handleMouseReleased(MouseEvent e) {
        mouseClicked = false;
        mouseButton = 0;
    }

    private void handleMouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public boolean isMouseClicked() {
        return mouseClicked;
    }

    public boolean isMouseJustClicked() {
        return mouseJustClicked;
    }

    public int getMouseButton() {
        return mouseButton;
    }

    public boolean isLeftClickJustPressed() {
        return mouseJustClicked && mouseButton == MouseEvent.BUTTON1;
    }

    public boolean isRightClickJustPressed() {
        return mouseJustClicked && mouseButton == MouseEvent.BUTTON3;
    }

    // ========== LIFECYCLE METHODS ==========

    public void update() {
        justPressedKeys.clear();
        mouseJustClicked = false;
    }

    public void reset() {
        pressedKeys.clear();
        justPressedKeys.clear();
        mouseX = 0;
        mouseY = 0;
        mouseClicked = false;
        mouseJustClicked = false;
        mouseButton = 0;
    }

    // ========== DEBUG METHODS ==========

    public Set<String> getPressedKeys() {
        return new HashSet<>(pressedKeys);
    }

    public String getMouseStateString() {
        return String.format("Mouse[x=%d, y=%d, clicked=%b, button=%d]",
                mouseX, mouseY, mouseClicked, mouseButton);
    }
}