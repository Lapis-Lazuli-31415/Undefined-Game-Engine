package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JPanel;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for InputManager
 *
 * Tests:
 * - Key press detection
 * - Key release detection
 * - Just pressed vs held down
 * - Case-insensitive key names
 * - Special key handling
 *
 * @author Wanru Cheng
 */
class InputManagerTest {

    private InputManager inputManager;
    private JPanel testPanel;

    @BeforeEach
    void setUp() {
        inputManager = new InputManager();
        testPanel = new JPanel();
        testPanel.addKeyListener(inputManager);
    }

    @Test
    void testInitialState() {
        assertFalse(inputManager.isKeyPressed("W"));
        assertFalse(inputManager.isKeyJustPressed("W"));
    }

    @Test
    void testKeyPressed() {
        KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_W, 'W');
        inputManager.keyPressed(event);

        assertTrue(inputManager.isKeyPressed("W"));
        assertTrue(inputManager.isKeyJustPressed("W"));
    }

    @Test
    void testKeyReleased() {
        // Press key
        KeyEvent pressEvent = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_W, 'W');
        inputManager.keyPressed(pressEvent);
        assertTrue(inputManager.isKeyPressed("W"));

        // Release key
        KeyEvent releaseEvent = createKeyEvent(KeyEvent.KEY_RELEASED, KeyEvent.VK_W, 'W');
        inputManager.keyReleased(releaseEvent);
        assertFalse(inputManager.isKeyPressed("W"));
    }

    @Test
    void testJustPressedClearedAfterUpdate() {
        KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_W, 'W');
        inputManager.keyPressed(event);

        assertTrue(inputManager.isKeyJustPressed("W"));

        // After update, just pressed should be cleared
        inputManager.update();
        assertFalse(inputManager.isKeyJustPressed("W"));

        // But key should still be pressed
        assertTrue(inputManager.isKeyPressed("W"));
    }

    @Test
    void testCaseInsensitive() {
        KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_W, 'W');
        inputManager.keyPressed(event);

        // All variations should work
        assertTrue(inputManager.isKeyPressed("W"));
        assertTrue(inputManager.isKeyPressed("w"));
        assertTrue(inputManager.isKeyJustPressed("W"));
        assertTrue(inputManager.isKeyJustPressed("w"));
    }

    @Test
    void testMultipleKeys() {
        KeyEvent eventW = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_W, 'W');
        KeyEvent eventA = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_A, 'A');

        inputManager.keyPressed(eventW);
        inputManager.keyPressed(eventA);

        assertTrue(inputManager.isKeyPressed("W"));
        assertTrue(inputManager.isKeyPressed("A"));
        assertTrue(inputManager.isKeyJustPressed("W"));
        assertTrue(inputManager.isKeyJustPressed("A"));
    }

    @Test
    void testSpaceKey() {
        KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_SPACE, ' ');
        inputManager.keyPressed(event);

        assertTrue(inputManager.isKeyPressed("SPACE"));
        assertTrue(inputManager.isKeyPressed("space"));
    }

    @Test
    void testEnterKey() {
        KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_ENTER, '\n');
        inputManager.keyPressed(event);

        assertTrue(inputManager.isKeyPressed("ENTER"));
        assertTrue(inputManager.isKeyPressed("enter"));
    }

    @Test
    void testEscapeKey() {
        KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_ESCAPE, KeyEvent.CHAR_UNDEFINED);
        inputManager.keyPressed(event);

        assertTrue(inputManager.isKeyPressed("ESCAPE"));
        assertTrue(inputManager.isKeyPressed("escape"));
    }

    @Test
    void testArrowKeys() {
        // Test Up arrow
        KeyEvent upEvent = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_UP, KeyEvent.CHAR_UNDEFINED);
        inputManager.keyPressed(upEvent);
        assertTrue(inputManager.isKeyPressed("UP"));

        // Release and test Down
        inputManager.reset();
        KeyEvent downEvent = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_DOWN, KeyEvent.CHAR_UNDEFINED);
        inputManager.keyPressed(downEvent);
        assertTrue(inputManager.isKeyPressed("DOWN"));

        // Test Left
        inputManager.reset();
        KeyEvent leftEvent = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);
        inputManager.keyPressed(leftEvent);
        assertTrue(inputManager.isKeyPressed("LEFT"));

        // Test Right
        inputManager.reset();
        KeyEvent rightEvent = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);
        inputManager.keyPressed(rightEvent);
        assertTrue(inputManager.isKeyPressed("RIGHT"));
    }

    @Test
    void testShiftKey() {
        KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_SHIFT, KeyEvent.CHAR_UNDEFINED);
        inputManager.keyPressed(event);

        assertTrue(inputManager.isKeyPressed("SHIFT"));
    }

    @Test
    void testControlKey() {
        KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_CONTROL, KeyEvent.CHAR_UNDEFINED);
        inputManager.keyPressed(event);

        assertTrue(inputManager.isKeyPressed("CONTROL"));
    }

    @Test
    void testAltKey() {
        KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_ALT, KeyEvent.CHAR_UNDEFINED);
        inputManager.keyPressed(event);

        assertTrue(inputManager.isKeyPressed("ALT"));
    }

    @Test
    void testNumberKeys() {
        for (int i = 0; i <= 9; i++) {
            inputManager.reset();

            int keyCode = KeyEvent.VK_0 + i;
            KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, keyCode, (char) ('0' + i));
            inputManager.keyPressed(event);

            assertTrue(inputManager.isKeyPressed(String.valueOf(i)));
        }
    }

    @Test
    void testReset() {
        KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_W, 'W');
        inputManager.keyPressed(event);

        assertTrue(inputManager.isKeyPressed("W"));

        inputManager.reset();

        assertFalse(inputManager.isKeyPressed("W"));
        assertFalse(inputManager.isKeyJustPressed("W"));
    }

    @Test
    void testNullKey() {
        assertFalse(inputManager.isKeyPressed(null));
        assertFalse(inputManager.isKeyJustPressed(null));
    }

    @Test
    void testKeyHeldDown() {
        KeyEvent pressEvent = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_W, 'W');

        // First press
        inputManager.keyPressed(pressEvent);
        assertTrue(inputManager.isKeyPressed("W"));
        assertTrue(inputManager.isKeyJustPressed("W"));

        // Update (simulating next frame)
        inputManager.update();

        // Key is still pressed but not "just pressed"
        assertTrue(inputManager.isKeyPressed("W"));
        assertFalse(inputManager.isKeyJustPressed("W"));

        // Simulate holding (pressing again while already pressed)
        inputManager.keyPressed(pressEvent);

        // Still pressed but NOT just pressed (because it was already down)
        assertTrue(inputManager.isKeyPressed("W"));
        assertFalse(inputManager.isKeyJustPressed("W"));
    }

    @Test
    void testGetDebugInfo() {
        String debugInfo = inputManager.getDebugInfo();
        assertNotNull(debugInfo);
        assertTrue(debugInfo.contains("Pressed"));
        assertTrue(debugInfo.contains("Just Pressed"));
    }

    /**
     * Helper method to create KeyEvent
     */
    private KeyEvent createKeyEvent(int id, int keyCode, char keyChar) {
        return new KeyEvent(
                testPanel,
                id,
                System.currentTimeMillis(),
                0,
                keyCode,
                keyChar
        );
    }
}