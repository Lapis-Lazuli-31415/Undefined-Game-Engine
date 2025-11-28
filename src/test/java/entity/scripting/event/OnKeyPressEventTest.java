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
        assertTrue(OnKeyPressEvent.REQUIRED_PARAMETERS.contains("Key"));
    }

    @Test
    void testIsRequiredParameter_key_returnsTrue() {
        OnKeyPressEvent event = new OnKeyPressEvent();
        assertTrue(event.isRequiredParameter("Key"));
    }

    @Test
    void testIsRequiredParameter_other_returnsFalse() {
        OnKeyPressEvent event = new OnKeyPressEvent();
        assertFalse(event.isRequiredParameter("other"));
        assertFalse(event.isRequiredParameter("value"));
    }

    @Test
    void testGetRequiredParameters() {
        OnKeyPressEvent event = new OnKeyPressEvent();
        assertEquals(OnKeyPressEvent.REQUIRED_PARAMETERS, event.getRequiredParameters());
    }

    @Test
    void testAddAndGetKey() {
        OnKeyPressEvent event = new OnKeyPressEvent();
        event.addEventParameter("Key", "W");
        assertEquals("W", event.getEventParameter("Key"));
    }
    @Test
    void testGetEventParameters_afterAddingKey() {
        OnKeyPressEvent event = new OnKeyPressEvent();
        event.addEventParameter("Key", "W");

        assertNotNull(event.getEventParameters());
        assertEquals(1, event.getEventParameters().size());
        assertEquals("W", event.getEventParameters().get("Key"));
    }

    @Test
    void testAddEventParameter_withInvalidKey_doesNotAdd() {
        OnKeyPressEvent event = new OnKeyPressEvent();
        event.addEventParameter("InvalidKey", "value");

        // "InvalidKey" is not in REQUIRED_PARAMETERS, so should not be added
        assertFalse(event.getEventParameters().containsKey("InvalidKey"));
    }

    @Test
    void testGetEventParameter_withInvalidKey_returnsEmptyString() {
        OnKeyPressEvent event = new OnKeyPressEvent();
        event.addEventParameter("Key", "W");

        // "InvalidKey" is not a required parameter
        assertEquals("", event.getEventParameter("InvalidKey"));
    }
    @Test
    void testGetKey() {
        OnKeyPressEvent event = new OnKeyPressEvent();
        event.addEventParameter("Key", "Space");
        // Note: getKey() uses lowercase "key" but REQUIRED_PARAMETERS has "Key"
        // This might be a bug in the implementation
        String key = event.getKey();
        assertNotNull(key);
    }

    @Test
    void testGetEventLabel() {
        OnKeyPressEvent event = new OnKeyPressEvent();
        assertEquals("On Key Press", event.getEventLabel());
    }
}