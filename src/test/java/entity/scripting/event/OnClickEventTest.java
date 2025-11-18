package entity.scripting.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for OnClickEvent
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
    void testToString() {
        OnClickEvent event = new OnClickEvent();
        String str = event.toString();

        assertTrue(str.contains("OnClickEvent"));
    }

    @Test
    void testMultipleInstances() {
        OnClickEvent event1 = new OnClickEvent();
        OnClickEvent event2 = new OnClickEvent();

        assertNotNull(event1);
        assertNotNull(event2);
        assertNotSame(event1, event2);
    }
}