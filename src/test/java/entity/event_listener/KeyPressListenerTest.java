package entity.event_listener;

import entity.InputManager;
import entity.scripting.event.OnKeyPressEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JPanel;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for KeyPressListener.
 *
 * @author Wanru Cheng
 */
class KeyPressListenerTest {

    private InputManager inputManager;
    private JPanel dummyComponent;

    @BeforeEach
    void setUp() {
        inputManager = new InputManager();
        dummyComponent = new JPanel();
    }

    // Helper method to create OnKeyPressEvent with a key
    private OnKeyPressEvent createKeyPressEvent(String key) {
        OnKeyPressEvent event = new OnKeyPressEvent();
        event.addEventParameter("Key", key);
        return event;
    }

    @Test
    void isTriggered_keyNotPressed_returnsFalse() {
        OnKeyPressEvent event = createKeyPressEvent("W");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertFalse(listener.isTriggered());
    }

    @Test
    void normalizeKey_nullKey_returnsUnknown() {
        OnKeyPressEvent event = new OnKeyPressEvent();
        // Don't add any parameter - key will be empty/null
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("UNKNOWN", listener.getKey());
    }

    @Test
    void normalizeKey_emptyKey_returnsUnknown() {
        OnKeyPressEvent event = createKeyPressEvent("");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("UNKNOWN", listener.getKey());
    }

    @Test
    void normalizeKey_whitespaceKey_returnsUnknown() {
        OnKeyPressEvent event = createKeyPressEvent("   ");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("UNKNOWN", listener.getKey());
    }

    @Test
    void handleKeyVariations_spacebar_returnsSpace() {
        OnKeyPressEvent event = createKeyPressEvent("spacebar");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("SPACE", listener.getKey());
    }

    @Test
    void handleKeyVariations_spaceBar_returnsSpace() {
        OnKeyPressEvent event = createKeyPressEvent("space bar");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("SPACE", listener.getKey());
    }

    @Test
    void handleKeyVariations_esc_returnsEscape() {
        OnKeyPressEvent event = createKeyPressEvent("esc");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("ESCAPE", listener.getKey());
    }

    @Test
    void handleKeyVariations_ctrl_returnsControl() {
        OnKeyPressEvent event = createKeyPressEvent("ctrl");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("CONTROL", listener.getKey());
    }

    @Test
    void handleKeyVariations_ctl_returnsControl() {
        OnKeyPressEvent event = createKeyPressEvent("ctl");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("CONTROL", listener.getKey());
    }

    @Test
    void handleKeyVariations_option_returnsAlt() {
        OnKeyPressEvent event = createKeyPressEvent("option");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("ALT", listener.getKey());
    }

    @Test
    void handleKeyVariations_upArrow_returnsUp() {
        OnKeyPressEvent event = createKeyPressEvent("uparrow");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("UP", listener.getKey());
    }

    @Test
    void handleKeyVariations_upArrowWithSpace_returnsUp() {
        OnKeyPressEvent event = createKeyPressEvent("up arrow");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("UP", listener.getKey());
    }

    @Test
    void handleKeyVariations_downArrow_returnsDown() {
        OnKeyPressEvent event = createKeyPressEvent("downarrow");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("DOWN", listener.getKey());
    }

    @Test
    void handleKeyVariations_downArrowWithSpace_returnsDown() {
        OnKeyPressEvent event = createKeyPressEvent("down arrow");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("DOWN", listener.getKey());
    }

    @Test
    void handleKeyVariations_leftArrow_returnsLeft() {
        OnKeyPressEvent event = createKeyPressEvent("leftarrow");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("LEFT", listener.getKey());
    }

    @Test
    void handleKeyVariations_leftArrowWithSpace_returnsLeft() {
        OnKeyPressEvent event = createKeyPressEvent("left arrow");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("LEFT", listener.getKey());
    }

    @Test
    void handleKeyVariations_rightArrow_returnsRight() {
        OnKeyPressEvent event = createKeyPressEvent("rightarrow");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("RIGHT", listener.getKey());
    }

    @Test
    void handleKeyVariations_rightArrowWithSpace_returnsRight() {
        OnKeyPressEvent event = createKeyPressEvent("right arrow");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("RIGHT", listener.getKey());
    }

    @Test
    void handleKeyVariations_return_returnsEnter() {
        OnKeyPressEvent event = createKeyPressEvent("return");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("ENTER", listener.getKey());
    }

    @Test
    void isTriggered_correctKeyPressed_returnsTrue() {
        OnKeyPressEvent event = createKeyPressEvent("W");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        KeyEvent keyEvent = new KeyEvent(
                dummyComponent, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0,
                KeyEvent.VK_W, 'W'
        );
        inputManager.keyPressed(keyEvent);

        assertTrue(listener.isTriggered());
    }

    @Test
    void isTriggered_wrongKeyPressed_returnsFalse() {
        OnKeyPressEvent event = createKeyPressEvent("W");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        KeyEvent keyEvent = new KeyEvent(
                dummyComponent, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0,
                KeyEvent.VK_A, 'A'
        );
        inputManager.keyPressed(keyEvent);

        assertFalse(listener.isTriggered());
    }

    @Test
    void isTriggered_afterUpdate_returnsFalse() {
        OnKeyPressEvent event = createKeyPressEvent("W");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        KeyEvent keyEvent = new KeyEvent(
                dummyComponent, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0,
                KeyEvent.VK_W, 'W'
        );
        inputManager.keyPressed(keyEvent);
        inputManager.update();

        assertFalse(listener.isTriggered());
    }

    @Test
    void isTriggered_caseInsensitive() {
        OnKeyPressEvent event = createKeyPressEvent("w");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        KeyEvent keyEvent = new KeyEvent(
                dummyComponent, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0,
                KeyEvent.VK_W, 'W'
        );
        inputManager.keyPressed(keyEvent);

        assertTrue(listener.isTriggered());
    }

    @Test
    void getKey_returnsNormalizedKey() {
        OnKeyPressEvent event = createKeyPressEvent("w");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertEquals("W", listener.getKey());
    }

    @Test
    void toString_containsKeyName() {
        OnKeyPressEvent event = createKeyPressEvent("W");
        KeyPressListener listener = new KeyPressListener(event, inputManager);
        assertTrue(listener.toString().contains("W"));
    }
}