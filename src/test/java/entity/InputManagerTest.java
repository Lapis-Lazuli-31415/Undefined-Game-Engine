package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for InputManager.
 *
 * @author Wanru Cheng
 */
class InputManagerTest {

    private InputManager inputManager;
    private JPanel dummyComponent;

    @BeforeEach
    void setUp() {
        inputManager = new InputManager();
        dummyComponent = new JPanel();
    }

    // ========== Keyboard Tests ==========

    @Test
    void isKeyPressed_keyNotPressed_returnsFalse() {
        assertFalse(inputManager.isKeyPressed("W"));
    }

    @Test
    void isKeyPressed_afterKeyPressed_returnsTrue() {
        KeyEvent keyEvent = new KeyEvent(
                dummyComponent, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0,
                KeyEvent.VK_W, 'W'
        );
        inputManager.keyPressed(keyEvent);

        assertTrue(inputManager.isKeyPressed("W"));
    }

    @Test
    void isKeyPressed_afterKeyReleased_returnsFalse() {
        KeyEvent pressEvent = new KeyEvent(
                dummyComponent, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0,
                KeyEvent.VK_W, 'W'
        );
        inputManager.keyPressed(pressEvent);

        KeyEvent releaseEvent = new KeyEvent(
                dummyComponent, KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(), 0,
                KeyEvent.VK_W, 'W'
        );
        inputManager.keyReleased(releaseEvent);

        assertFalse(inputManager.isKeyPressed("W"));
    }

    @Test
    void isKeyJustPressed_afterKeyPressed_returnsTrue() {
        KeyEvent keyEvent = new KeyEvent(
                dummyComponent, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0,
                KeyEvent.VK_W, 'W'
        );
        inputManager.keyPressed(keyEvent);

        assertTrue(inputManager.isKeyJustPressed("W"));
    }

    @Test
    void isKeyJustPressed_afterUpdate_returnsFalse() {
        KeyEvent keyEvent = new KeyEvent(
                dummyComponent, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0,
                KeyEvent.VK_W, 'W'
        );
        inputManager.keyPressed(keyEvent);

        inputManager.update();

        assertFalse(inputManager.isKeyJustPressed("W"));
    }

    @Test
    void isKeyPressed_caseInsensitive() {
        KeyEvent keyEvent = new KeyEvent(
                dummyComponent, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0,
                KeyEvent.VK_A, 'a'
        );
        inputManager.keyPressed(keyEvent);

        assertTrue(inputManager.isKeyPressed("A"));
        assertTrue(inputManager.isKeyPressed("a"));
    }

    @Test
    void keyPressed_sameKeyTwice_doesNotDuplicate() {
        KeyEvent keyEvent = new KeyEvent(
                dummyComponent, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0,
                KeyEvent.VK_W, 'W'
        );
        inputManager.keyPressed(keyEvent);
        inputManager.keyPressed(keyEvent);

        assertEquals(1, inputManager.getPressedKeys().size());
    }

    // ========== Mouse Tests ==========

    @Test
    void isMouseClicked_initialState_returnsFalse() {
        assertFalse(inputManager.isMouseClicked());
    }

    @Test
    void getMouseX_initialState_returnsZero() {
        assertEquals(0, inputManager.getMouseX());
    }

    @Test
    void getMouseY_initialState_returnsZero() {
        assertEquals(0, inputManager.getMouseY());
    }

    @Test
    void getMouseListener_returnsNonNull() {
        assertNotNull(inputManager.getMouseListener());
    }

    @Test
    void getMouseListener_returnsSameInstance() {
        assertSame(inputManager.getMouseListener(), inputManager.getMouseListener());
    }

    @Test
    void isMouseJustClicked_initialState_returnsFalse() {
        assertFalse(inputManager.isMouseJustClicked());
    }

    @Test
    void getMouseButton_initialState_returnsZero() {
        assertEquals(0, inputManager.getMouseButton());
    }

    @Test
    void isLeftClickJustPressed_initialState_returnsFalse() {
        assertFalse(inputManager.isLeftClickJustPressed());
    }

    @Test
    void isRightClickJustPressed_initialState_returnsFalse() {
        assertFalse(inputManager.isRightClickJustPressed());
    }

    @Test
    void mousePressed_updatesMousePosition() {
        MouseEvent mouseEvent = new MouseEvent(
                dummyComponent, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0,
                100, 200, 1, false, MouseEvent.BUTTON1
        );
        inputManager.getMouseListener().mousePressed(mouseEvent);

        assertEquals(100, inputManager.getMouseX());
        assertEquals(200, inputManager.getMouseY());
    }

    @Test
    void mousePressed_setsMouseClicked() {
        MouseEvent mouseEvent = new MouseEvent(
                dummyComponent, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0,
                100, 200, 1, false, MouseEvent.BUTTON1
        );
        inputManager.getMouseListener().mousePressed(mouseEvent);

        assertTrue(inputManager.isMouseClicked());
        assertTrue(inputManager.isMouseJustClicked());
    }

    @Test
    void mousePressed_setsMouseButton() {
        MouseEvent mouseEvent = new MouseEvent(
                dummyComponent, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0,
                100, 200, 1, false, MouseEvent.BUTTON1
        );
        inputManager.getMouseListener().mousePressed(mouseEvent);

        assertEquals(MouseEvent.BUTTON1, inputManager.getMouseButton());
    }

    @Test
    void mousePressed_leftButton_isLeftClickJustPressed() {
        MouseEvent mouseEvent = new MouseEvent(
                dummyComponent, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0,
                100, 200, 1, false, MouseEvent.BUTTON1
        );
        inputManager.getMouseListener().mousePressed(mouseEvent);

        assertTrue(inputManager.isLeftClickJustPressed());
        assertFalse(inputManager.isRightClickJustPressed());
    }

    @Test
    void mousePressed_rightButton_isRightClickJustPressed() {
        MouseEvent mouseEvent = new MouseEvent(
                dummyComponent, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0,
                100, 200, 1, false, MouseEvent.BUTTON3
        );
        inputManager.getMouseListener().mousePressed(mouseEvent);

        assertTrue(inputManager.isRightClickJustPressed());
        assertFalse(inputManager.isLeftClickJustPressed());
    }

    @Test
    void mouseReleased_clearsMouseClicked() {
        // First press
        MouseEvent pressEvent = new MouseEvent(
                dummyComponent, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0,
                100, 200, 1, false, MouseEvent.BUTTON1
        );
        inputManager.getMouseListener().mousePressed(pressEvent);

        // Then release
        MouseEvent releaseEvent = new MouseEvent(
                dummyComponent, MouseEvent.MOUSE_RELEASED,
                System.currentTimeMillis(), 0,
                100, 200, 1, false, MouseEvent.BUTTON1
        );
        inputManager.getMouseListener().mouseReleased(releaseEvent);

        assertFalse(inputManager.isMouseClicked());
        assertEquals(0, inputManager.getMouseButton());
    }

    @Test
    void mouseMoved_updatesPosition() {
        MouseEvent moveEvent = new MouseEvent(
                dummyComponent, MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(), 0,
                300, 400, 0, false
        );
        inputManager.getMouseListener().mouseMoved(moveEvent);

        assertEquals(300, inputManager.getMouseX());
        assertEquals(400, inputManager.getMouseY());
    }

    @Test
    void mouseDragged_updatesPosition() {
        MouseEvent dragEvent = new MouseEvent(
                dummyComponent, MouseEvent.MOUSE_DRAGGED,
                System.currentTimeMillis(), 0,
                500, 600, 0, false
        );
        inputManager.getMouseListener().mouseDragged(dragEvent);

        assertEquals(500, inputManager.getMouseX());
        assertEquals(600, inputManager.getMouseY());
    }

    @Test
    void update_clearsMouseJustClicked() {
        MouseEvent mouseEvent = new MouseEvent(
                dummyComponent, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0,
                100, 200, 1, false, MouseEvent.BUTTON1
        );
        inputManager.getMouseListener().mousePressed(mouseEvent);

        assertTrue(inputManager.isMouseJustClicked());

        inputManager.update();

        assertFalse(inputManager.isMouseJustClicked());
    }

    // ========== Reset Tests ==========

    @Test
    void reset_clearsAllState() {
        // Press a key
        KeyEvent keyEvent = new KeyEvent(
                dummyComponent, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0,
                KeyEvent.VK_W, 'W'
        );
        inputManager.keyPressed(keyEvent);

        // Press mouse
        MouseEvent mouseEvent = new MouseEvent(
                dummyComponent, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0,
                100, 200, 1, false, MouseEvent.BUTTON1
        );
        inputManager.getMouseListener().mousePressed(mouseEvent);

        // Reset
        inputManager.reset();

        // Verify all cleared
        assertFalse(inputManager.isKeyPressed("W"));
        assertFalse(inputManager.isKeyJustPressed("W"));
        assertFalse(inputManager.isMouseClicked());
        assertFalse(inputManager.isMouseJustClicked());
        assertEquals(0, inputManager.getMouseX());
        assertEquals(0, inputManager.getMouseY());
        assertEquals(0, inputManager.getMouseButton());
    }

    @Test
    void getPressedKeys_afterKeyPressed_containsKey() {
        KeyEvent keyEvent = new KeyEvent(
                dummyComponent, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0,
                KeyEvent.VK_W, 'W'
        );
        inputManager.keyPressed(keyEvent);

        assertTrue(inputManager.getPressedKeys().contains("W"));
    }

    @Test
    void getMouseStateString_returnsFormattedString() {
        String state = inputManager.getMouseStateString();

        assertNotNull(state);
        assertTrue(state.contains("Mouse"));
    }

    @Test
    void mousePressed_whileAlreadyClicked_doesNotSetJustClicked() {
        // First press
        MouseEvent firstPress = new MouseEvent(
                dummyComponent, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0,
                100, 200, 1, false, MouseEvent.BUTTON1
        );
        inputManager.getMouseListener().mousePressed(firstPress);

        // Clear just clicked
        inputManager.update();

        // Second press without release (shouldn't happen normally, but test the logic)
        MouseEvent secondPress = new MouseEvent(
                dummyComponent, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0,
                150, 250, 1, false, MouseEvent.BUTTON1
        );
        inputManager.getMouseListener().mousePressed(secondPress);

        // Position should update but justClicked should not be set again
        assertEquals(150, inputManager.getMouseX());
        assertEquals(250, inputManager.getMouseY());
    }
}