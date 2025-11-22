package entity.scripting.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for OnKeyPressEvent
 *
 * @author Wanru Cheng
 */
class OnKeyPressEventTest {

    @Test
    void testConstructor() {
        OnKeyPressEvent event = new OnKeyPressEvent("W");

        assertEquals("W", event.getKey());
    }

    @Test
    void testGetKey() {
        OnKeyPressEvent event = new OnKeyPressEvent("Space");

        assertEquals("Space", event.getKey());
    }

    @Test
    void testGetKeyPreservesOriginal() {
        // Test that original input is preserved (not normalized)
        OnKeyPressEvent event1 = new OnKeyPressEvent("w");
        assertEquals("w", event1.getKey());

        OnKeyPressEvent event2 = new OnKeyPressEvent("W");
        assertEquals("W", event2.getKey());

        OnKeyPressEvent event3 = new OnKeyPressEvent("Space");
        assertEquals("Space", event3.getKey());
    }

    @Test
    void testToString() {
        OnKeyPressEvent event = new OnKeyPressEvent("W");
        String str = event.toString();

        assertTrue(str.contains("OnKeyPressEvent"));
        assertTrue(str.contains("W"));
    }

    @Test
    void testDifferentKeys() {
        String[] keys = {"W", "A", "S", "D", "Space", "Enter", "Escape", "Up", "Down"};

        for (String key : keys) {
            OnKeyPressEvent event = new OnKeyPressEvent(key);
            assertEquals(key, event.getKey());
        }
    }

    @Test
    void testNullKey() {
        OnKeyPressEvent event = new OnKeyPressEvent(null);
        assertNull(event.getKey());
    }

    @Test
    void testEmptyKey() {
        OnKeyPressEvent event = new OnKeyPressEvent("");
        assertEquals("", event.getKey());
    }
}