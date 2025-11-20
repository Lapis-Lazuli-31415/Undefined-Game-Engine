package entity.Eventlistener;

import entity.InputManager;
import entity.scripting.event.OnKeyPressEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import javax.swing.JPanel;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for KeyPressListener
 *
 * Tests:
 * - Case-insensitive key matching
 * - Key name normalization
 * - Common key variations
 * - Integration with InputManager
 *
 * @author Wanru Cheng
 */
class KeyPressListenerTest {

    private InputManager inputManager;
    private JPanel testPanel;

    @BeforeEach
    void setUp() {
        inputManager = new InputManager();
        testPanel = new JPanel();
        testPanel.addKeyListener(inputManager);
    }

    @Test
    void testCaseInsensitiveLowercase() {
        OnKeyPressEvent event = new OnKeyPressEvent("w");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("W", listener.getKey());
    }

    @Test
    void testCaseInsensitiveUppercase() {
        OnKeyPressEvent event = new OnKeyPressEvent("W");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("W", listener.getKey());
    }

    @Test
    void testCaseInsensitiveMixedCase() {
        OnKeyPressEvent event = new OnKeyPressEvent("SpAcE");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("SPACE", listener.getKey());
    }

    @Test
    void testKeyWithWhitespace() {
        OnKeyPressEvent event = new OnKeyPressEvent("  w  ");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("W", listener.getKey());
    }

    @Test
    void testSpaceVariations() {
        // Test "space"
        OnKeyPressEvent event1 = new OnKeyPressEvent("space");
        KeyPressListener listener1 = new KeyPressListener(event1, inputManager);
        assertEquals("SPACE", listener1.getKey());

        // Test "spacebar"
        OnKeyPressEvent event2 = new OnKeyPressEvent("spacebar");
        KeyPressListener listener2 = new KeyPressListener(event2, inputManager);
        assertEquals("SPACE", listener2.getKey());

        // Test "SPACE BAR"
        OnKeyPressEvent event3 = new OnKeyPressEvent("SPACE BAR");
        KeyPressListener listener3 = new KeyPressListener(event3, inputManager);
        assertEquals("SPACE", listener3.getKey());
    }

    @Test
    void testEscapeVariations() {
        // Test "esc"
        OnKeyPressEvent event1 = new OnKeyPressEvent("esc");
        KeyPressListener listener1 = new KeyPressListener(event1, inputManager);
        assertEquals("ESCAPE", listener1.getKey());

        // Test "escape"
        OnKeyPressEvent event2 = new OnKeyPressEvent("escape");
        KeyPressListener listener2 = new KeyPressListener(event2, inputManager);
        assertEquals("ESCAPE", listener2.getKey());
    }

    @Test
    void testControlVariations() {
        // Test "ctrl"
        OnKeyPressEvent event1 = new OnKeyPressEvent("ctrl");
        KeyPressListener listener1 = new KeyPressListener(event1, inputManager);
        assertEquals("CONTROL", listener1.getKey());

        // Test "control"
        OnKeyPressEvent event2 = new OnKeyPressEvent("control");
        KeyPressListener listener2 = new KeyPressListener(event2, inputManager);
        assertEquals("CONTROL", listener2.getKey());
    }

    @Test
    void testArrowKeyVariations() {
        // Test "up"
        OnKeyPressEvent event1 = new OnKeyPressEvent("up");
        KeyPressListener listener1 = new KeyPressListener(event1, inputManager);
        assertEquals("UP", listener1.getKey());

        // Test "uparrow"
        OnKeyPressEvent event2 = new OnKeyPressEvent("uparrow");
        KeyPressListener listener2 = new KeyPressListener(event2, inputManager);
        assertEquals("UP", listener2.getKey());

        // Test "up arrow"
        OnKeyPressEvent event3 = new OnKeyPressEvent("up arrow");
        KeyPressListener listener3 = new KeyPressListener(event3, inputManager);
        assertEquals("UP", listener3.getKey());
    }

    @Test
    void testEnterVariations() {
        // Test "enter"
        OnKeyPressEvent event1 = new OnKeyPressEvent("enter");
        KeyPressListener listener1 = new KeyPressListener(event1, inputManager);
        assertEquals("ENTER", listener1.getKey());

        // Test "return"
        OnKeyPressEvent event2 = new OnKeyPressEvent("return");
        KeyPressListener listener2 = new KeyPressListener(event2, inputManager);
        assertEquals("ENTER", listener2.getKey());
    }

    @Test
    void testNumberKeys() {
        for (int i = 0; i <= 9; i++) {
            OnKeyPressEvent event = new OnKeyPressEvent(String.valueOf(i));
            KeyPressListener listener = new KeyPressListener(event, inputManager);
            assertEquals(String.valueOf(i), listener.getKey());
        }
    }

    @Test
    void testAlphabetKeys() {
        for (char c = 'a'; c <= 'z'; c++) {
            OnKeyPressEvent event = new OnKeyPressEvent(String.valueOf(c));
            KeyPressListener listener = new KeyPressListener(event, inputManager);
            assertEquals(String.valueOf(c).toUpperCase(), listener.getKey());
        }
    }

    @Test
    void testEmptyKey() {
        OnKeyPressEvent event = new OnKeyPressEvent("");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("UNKNOWN", listener.getKey());
    }

    @Test
    void testNullKey() {
        OnKeyPressEvent event = new OnKeyPressEvent(null);
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("UNKNOWN", listener.getKey());
    }

    @Test
    void testIsTriggeredWhenKeyNotPressed() {
        OnKeyPressEvent event = new OnKeyPressEvent("W");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertFalse(listener.isTriggered());
    }

    @Test
    void testIsTriggeredWhenKeyPressed() {
        OnKeyPressEvent event = new OnKeyPressEvent("W");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        // Simulate key press
        KeyEvent keyEvent = new KeyEvent(
                testPanel,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_W,
                'W'
        );
        inputManager.keyPressed(keyEvent);

        assertTrue(listener.isTriggered());

        // After update, should not trigger again
        inputManager.update();
        assertFalse(listener.isTriggered());
    }

    @Test
    void testToString() {
        OnKeyPressEvent event = new OnKeyPressEvent("W");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        String str = listener.toString();
        assertTrue(str.contains("KeyPressListener"));
        assertTrue(str.contains("W"));
    }
}