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
        OnKeyPressEvent event = new OnKeyPressEvent();
        assertNotNull(event);
    }

    @Test
    void testGetEventType() {
        String eventType = OnKeyPressEvent.getEventType();
        assertEquals("On Key Press", eventType);
    }

    @Test
    void testEventTypeConstant() {
        assertEquals("On Key Press", OnKeyPressEvent.EVENT_TYPE);
    }

    @Test
    void testRequiredParameters() {
        assertEquals(1, OnKeyPressEvent.REQUIRED_PARAMETERS.size());
        assertTrue(OnKeyPressEvent.REQUIRED_PARAMETERS.contains("key"));
    }

    @Test
    void testIsRequiredParameter_key_returnsTrue() {
        OnKeyPressEvent event = new OnKeyPressEvent();
        assertTrue(event.isRequiredParameter("key"));
    }

    @Test
    void testIsRequiredParameter_other_returnsFalse() {
        OnKeyPressEvent event = new OnKeyPressEvent();
        assertFalse(event.isRequiredParameter("other"));
        assertFalse(event.isRequiredParameter("value"));
        assertFalse(event.isRequiredParameter(""));
    }

    @Test
    void testGetRequiredParameters() {
        OnKeyPressEvent event = new OnKeyPressEvent();
        assertEquals(OnKeyPressEvent.REQUIRED_PARAMETERS, event.getRequiredParameters());
    }

    @Test
    void testToString() {
        OnKeyPressEvent event = new OnKeyPressEvent();
        String str = event.toString();

        assertNotNull(str);
        assertTrue(str.contains("OnKeyPressEvent"));
    }
}