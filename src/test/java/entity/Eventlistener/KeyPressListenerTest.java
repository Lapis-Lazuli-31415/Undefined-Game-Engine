package entity.Eventlistener;

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

    @Test
    void isTriggered_keyNotPressed_returnsFalse() {
        // Arrange
        OnKeyPressEvent event = new OnKeyPressEvent("W");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        // Assert
        assertFalse(listener.isTriggered());
    }
    @Test
    void normalizeKey_nullKey_returnsUnknown() {
        OnKeyPressEvent event = new OnKeyPressEvent(null);
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("UNKNOWN", listener.getKey());
    }

    @Test
    void normalizeKey_emptyKey_returnsUnknown() {
        OnKeyPressEvent event = new OnKeyPressEvent("");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("UNKNOWN", listener.getKey());
    }

    @Test
    void normalizeKey_whitespaceKey_returnsUnknown() {
        OnKeyPressEvent event = new OnKeyPressEvent("   ");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("UNKNOWN", listener.getKey());
    }

    @Test
    void handleKeyVariations_spacebar_returnsSpace() {
        OnKeyPressEvent event = new OnKeyPressEvent("spacebar");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("SPACE", listener.getKey());
    }

    @Test
    void handleKeyVariations_spaceBar_returnsSpace() {
        OnKeyPressEvent event = new OnKeyPressEvent("space bar");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("SPACE", listener.getKey());
    }

    @Test
    void handleKeyVariations_esc_returnsEscape() {
        OnKeyPressEvent event = new OnKeyPressEvent("esc");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("ESCAPE", listener.getKey());
    }

    @Test
    void handleKeyVariations_ctrl_returnsControl() {
        OnKeyPressEvent event = new OnKeyPressEvent("ctrl");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("CONTROL", listener.getKey());
    }

    @Test
    void handleKeyVariations_ctl_returnsControl() {
        OnKeyPressEvent event = new OnKeyPressEvent("ctl");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("CONTROL", listener.getKey());
    }

    @Test
    void handleKeyVariations_option_returnsAlt() {
        OnKeyPressEvent event = new OnKeyPressEvent("option");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("ALT", listener.getKey());
    }

    @Test
    void handleKeyVariations_upArrow_returnsUp() {
        OnKeyPressEvent event = new OnKeyPressEvent("uparrow");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("UP", listener.getKey());
    }

    @Test
    void handleKeyVariations_upArrowWithSpace_returnsUp() {
        OnKeyPressEvent event = new OnKeyPressEvent("up arrow");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("UP", listener.getKey());
    }

    @Test
    void handleKeyVariations_downArrow_returnsDown() {
        OnKeyPressEvent event = new OnKeyPressEvent("downarrow");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("DOWN", listener.getKey());
    }

    @Test
    void handleKeyVariations_downArrowWithSpace_returnsDown() {
        OnKeyPressEvent event = new OnKeyPressEvent("down arrow");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("DOWN", listener.getKey());
    }

    @Test
    void handleKeyVariations_leftArrow_returnsLeft() {
        OnKeyPressEvent event = new OnKeyPressEvent("leftarrow");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("LEFT", listener.getKey());
    }

    @Test
    void handleKeyVariations_leftArrowWithSpace_returnsLeft() {
        OnKeyPressEvent event = new OnKeyPressEvent("left arrow");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("LEFT", listener.getKey());
    }

    @Test
    void handleKeyVariations_rightArrow_returnsRight() {
        OnKeyPressEvent event = new OnKeyPressEvent("rightarrow");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("RIGHT", listener.getKey());
    }

    @Test
    void handleKeyVariations_rightArrowWithSpace_returnsRight() {
        OnKeyPressEvent event = new OnKeyPressEvent("right arrow");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("RIGHT", listener.getKey());
    }

    @Test
    void handleKeyVariations_return_returnsEnter() {
        OnKeyPressEvent event = new OnKeyPressEvent("return");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        assertEquals("ENTER", listener.getKey());
    }
    @Test
    void isTriggered_correctKeyPressed_returnsTrue() {
        // Arrange
        OnKeyPressEvent event = new OnKeyPressEvent("W");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        // Simulate key press
        KeyEvent keyEvent = new KeyEvent(
                dummyComponent, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0,
                KeyEvent.VK_W, 'W'
        );
        inputManager.keyPressed(keyEvent);

        // Assert
        assertTrue(listener.isTriggered());
    }

    @Test
    void isTriggered_wrongKeyPressed_returnsFalse() {
        // Arrange
        OnKeyPressEvent event = new OnKeyPressEvent("W");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        // Press different key
        KeyEvent keyEvent = new KeyEvent(
                dummyComponent, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0,
                KeyEvent.VK_A, 'A'
        );
        inputManager.keyPressed(keyEvent);

        // Assert
        assertFalse(listener.isTriggered());
    }

    @Test
    void isTriggered_afterUpdate_returnsFalse() {
        // Arrange
        OnKeyPressEvent event = new OnKeyPressEvent("W");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        // Press key
        KeyEvent keyEvent = new KeyEvent(
                dummyComponent, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0,
                KeyEvent.VK_W, 'W'
        );
        inputManager.keyPressed(keyEvent);

        // Update clears just pressed state
        inputManager.update();

        // Assert - should be false after update
        assertFalse(listener.isTriggered());
    }

    @Test
    void isTriggered_caseInsensitive() {
        // Arrange - listener for lowercase 'w'
        OnKeyPressEvent event = new OnKeyPressEvent("w");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        // Press uppercase W
        KeyEvent keyEvent = new KeyEvent(
                dummyComponent, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0,
                KeyEvent.VK_W, 'W'
        );
        inputManager.keyPressed(keyEvent);

        // Assert
        assertTrue(listener.isTriggered());
    }

//    @Test
//    void isTriggered_spaceKey() {
//        // Arrange
//        OnKeyPressEvent event = new OnKeyPressEvent("Space");
//        KeyPressListener listener = new KeyPressListener(event, inputManager);
//
//        // Press space
//        KeyEvent keyEvent = new KeyEvent(
//                dummyComponent, KeyEvent.KEY_PRESSED,
//                System.currentTimeMillis(), 0,
//                KeyEvent.VK_SPACE, ' '
//        );
//        inputManager.keyPressed(keyEvent);
//
//        // Assert
//        assertTrue(listener.isTriggered());
//    }

    @Test
    void getKey_returnsNormalizedKey() {
        // Arrange
        OnKeyPressEvent event = new OnKeyPressEvent("w");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        // Assert - should be uppercase
        assertEquals("W", listener.getKey());
    }

    @Test
    void toString_containsKeyName() {
        // Arrange
        OnKeyPressEvent event = new OnKeyPressEvent("W");
        KeyPressListener listener = new KeyPressListener(event, inputManager);

        // Assert
        assertTrue(listener.toString().contains("W"));
    }
}