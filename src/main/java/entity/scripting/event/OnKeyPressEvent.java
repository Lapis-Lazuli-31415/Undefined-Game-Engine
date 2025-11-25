package entity.scripting.event;

import java.util.List;

public class OnKeyPressEvent extends Event{
    public static final String EVENT_TYPE = "On Key Press";

    public OnKeyPressEvent() {
        super(EVENT_TYPE);
    }

    public static String getEventType() {
        return EVENT_TYPE;
    }

    @Override
    public boolean isRequiredParameter(String key){
        final List<String> REQUIRED_PARAMS = List.of("key");

        return REQUIRED_PARAMS.contains(key);
    }
}
