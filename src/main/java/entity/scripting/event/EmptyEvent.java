package entity.scripting.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmptyEvent extends Event{
    public static final String EVENT_TYPE = "Empty";

    public EmptyEvent() {
        super(EVENT_TYPE);
    }

    public static String getEventType() {
        return EVENT_TYPE;
    }

    @Override
    public boolean isRequiredParameter(String key) {
        return false;
    }
}
