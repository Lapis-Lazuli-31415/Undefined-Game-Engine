package entity.Eventlistener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClickListener
 *
 * Tests:
 * - Click detection
 * - Reset functionality
 * - Multiple clicks
 *
 * @author Wanru Cheng
 */
class ClickListenerTest {

    private ClickListener listener;

    @BeforeEach
    void setUp() {
        listener = new ClickListener("TestButton");
    }

    @Test
    void testInitialState() {
        assertFalse(listener.isTriggered());
    }

    @Test
    void testNotifyClicked() {
        listener.notifyClicked();
        assertTrue(listener.isTriggered());
    }

    @Test
    void testReset() {
        listener.notifyClicked();
        assertTrue(listener.isTriggered());

        listener.reset();
        assertFalse(listener.isTriggered());
    }

    @Test
    void testMultipleClicks() {
        // First click
        listener.notifyClicked();
        assertTrue(listener.isTriggered());

        // Reset
        listener.reset();
        assertFalse(listener.isTriggered());

        // Second click
        listener.notifyClicked();
        assertTrue(listener.isTriggered());
    }

    @Test
    void testMultipleNotifyBeforeReset() {
        listener.notifyClicked();
        listener.notifyClicked();
        listener.notifyClicked();

        // Should still be triggered
        assertTrue(listener.isTriggered());

        // Reset once
        listener.reset();
        assertFalse(listener.isTriggered());
    }

    @Test
    void testResetWithoutClick() {
        assertFalse(listener.isTriggered());

        listener.reset();
        assertFalse(listener.isTriggered());
    }

    @Test
    void testToString() {
        String str = listener.toString();
        assertTrue(str.contains("ClickListener"));
        assertTrue(str.contains("TestButton"));
    }
}