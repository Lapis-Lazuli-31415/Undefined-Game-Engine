package entity.scripting.event;

import java.util.Arrays;
import java.util.List;

public class OnKeyPressEvent extends Event{
    public static final String EVENT_TYPE = "On Key Press";
    public static final List<String> REQUIRED_PARAMETERS = List.of("key");

    public OnKeyPressEvent() {
        super(EVENT_TYPE);
    }

    public static String getEventType() {
        return EVENT_TYPE;
    }

    @Override
    public boolean isRequiredParameter(String key){
        return REQUIRED_PARAMETERS.contains(key);
    }

    @Override
    public List<String> getRequiredParameters() {
        return REQUIRED_PARAMETERS;
    }
}
