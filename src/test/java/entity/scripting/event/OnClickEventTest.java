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

    @Test
    void testEventTypeConstant() {
        assertEquals("On Click", OnClickEvent.EVENT_TYPE);
    }

    @Test
    void testGetEventLabel() {
        OnClickEvent event = new OnClickEvent();
        assertEquals("On Click", event.getEventLabel());
    }

    @Test
    void testIsRequiredParameter_alwaysReturnsFalse() {
        OnClickEvent event = new OnClickEvent();
        assertFalse(event.isRequiredParameter("any"));
        assertFalse(event.isRequiredParameter("key"));
        assertFalse(event.isRequiredParameter("Key"));
        assertFalse(event.isRequiredParameter(""));
    }

    @Test
    void testGetRequiredParameters_returnsEmptyList() {
        OnClickEvent event = new OnClickEvent();
        assertTrue(event.getRequiredParameters().isEmpty());
        assertEquals(0, event.getRequiredParameters().size());
    }

    @Test
    void testRequiredParametersConstant_isEmpty() {
        assertTrue(OnClickEvent.REQUIRED_PARAMETERS.isEmpty());
    }

    // Tests for Event base class methods via OnClickEvent

    @Test
    void testGetEventParameters_returnsEmptyMapInitially() {
        OnClickEvent event = new OnClickEvent();
        assertNotNull(event.getEventParameters());
        assertTrue(event.getEventParameters().isEmpty());
    }

    @Test
    void testAddEventParameter_withInvalidKey_doesNotAdd() {
        OnClickEvent event = new OnClickEvent();
        // OnClickEvent has no required parameters, so nothing should be added
        event.addEventParameter("anyKey", "anyValue");
        assertTrue(event.getEventParameters().isEmpty());
    }

    @Test
    void testGetEventParameter_withInvalidKey_returnsEmptyString() {
        OnClickEvent event = new OnClickEvent();
        // OnClickEvent.isRequiredParameter always returns false
        // So getEventParameter should return ""
        assertEquals("", event.getEventParameter("anyKey"));
        assertEquals("", event.getEventParameter("Key"));
    }
}