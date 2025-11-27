package entity.scripting.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for OnClickEvent.
 *
 * @author Wanru Cheng
 */
class OnClickEventTest {

    @Test
    void testConstructor() {
        OnClickEvent event = new OnClickEvent();

        assertNotNull(event);
    }

    @Test
    void testGetEventType() {
        String eventType = OnClickEvent.getEventType();

        assertEquals("On Click", eventType);
    }
}