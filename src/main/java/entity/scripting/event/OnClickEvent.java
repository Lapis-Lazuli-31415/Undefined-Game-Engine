package entity.scripting.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class OnClickEvent extends Event{
    public static final String EVENT_TYPE = "On Click";

    @JsonIgnore
    public static final List<String> REQUIRED_PARAMETERS = List.of();

    public OnClickEvent() {
        super(EVENT_TYPE);
    }

    public static String getEventType() {
        return EVENT_TYPE;
    }

    @JsonIgnore
    @Override
    public boolean isRequiredParameter(String key){
        return false;
    }

    @Override
    public List<String> getRequiredParameters() {
        return REQUIRED_PARAMETERS;
    }
}
