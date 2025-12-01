package view;

import entity.InputState;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * InputManager - Manages keyboard and mouse input state.
 * Part of View layer (Frameworks & Drivers).
 * Implements InputState interface from Entity layer.
 *
 * This class handles UI framework events (KeyEvent, MouseEvent)
 * and exposes state through the InputState interface.
 *
 * @author Wanru Cheng
 */
public class InputManager extends KeyAdapter implements InputState {

    // ===== Keyboard State =====
    private final Set<String> pressedKeys;
    private final Set<String> justPressedKeys;

    // ===== Mouse State =====
    private int mouseX;
    private int mouseY;
    private boolean mouseClicked;
    private boolean mouseJustClicked;
    private int mouseButton;
    private int canvasWidth = 800;
    private int canvasHeight = 600;

    private final MouseAdapter mouseAdapter;

    public InputManager() {
        this.pressedKeys = new HashSet<>();
        this.justPressedKeys = new HashSet<>();
        this.mouseX = 0;
        this.mouseY = 0;
        this.mouseClicked = false;
        this.mouseJustClicked = false;
        this.mouseButton = 0;

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

    @Override
    public boolean isKeyPressed(String key) {
        return pressedKeys.contains(key.toUpperCase());
    }

    @Override
    public boolean isKeyJustPressed(String key) {
        return justPressedKeys.contains(key.toUpperCase());
    }

    private String getKeyText(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {
            return "SPACE";
        }
        String keyText = KeyEvent.getKeyText(keyCode);
        return keyText.toUpperCase();
    }

    // ========== MOUSE METHODS ==========

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

    @Override
    public int getMouseX() {
        return mouseX;
    }

    @Override
    public int getMouseY() {
        return mouseY;
    }

    @Override
    public boolean isMouseClicked() {
        return mouseClicked;
    }

    @Override
    public boolean isMouseJustClicked() {
        return mouseJustClicked;
    }

    @Override
    public int getMouseButton() {
        return mouseButton;
    }

    @Override
    public boolean isLeftClickJustPressed() {
        return mouseJustClicked && mouseButton == MouseEvent.BUTTON1;
    }

    @Override
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

    public void setCanvasDimensions(int width, int height) {
        this.canvasWidth = width;
        this.canvasHeight = height;
    }

    @Override
    public int getCanvasWidth() {
        return canvasWidth;
    }

    @Override
    public int getCanvasHeight() {
        return canvasHeight;
    }

    // ========== DEBUG METHODS ==========

    @Override
    public Set<String> getPressedKeys() {
        return new HashSet<>(pressedKeys);
    }

    @Override
    public String getMouseStateString() {
        return String.format("Mouse[x=%d, y=%d, clicked=%b, button=%d]",
                mouseX, mouseY, mouseClicked, mouseButton);
    }
}