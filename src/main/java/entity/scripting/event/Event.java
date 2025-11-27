package entity.scripting.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class Event {
    private final String eventLabel;
    private final Map<String, String> eventParameters;

    public Event(String eventLabel) {
        this.eventLabel = eventLabel;
        eventParameters = new HashMap<>();
    }

    public Map<String, String> getEventParameters() {
        return eventParameters;
    }

    public void addEventParameter(String key, String value) {
        if (isRequiredParameter(key)) {
            eventParameters.put(key, value);
        }
    }

    public String getEventParameter(String key) {
        if (isRequiredParameter(key)) {
            return Objects.requireNonNullElse(eventParameters.get(key), "");
        } else {
            return "";
        }
    }

    public abstract boolean isRequiredParameter(String key);

    public abstract List<String> getRequiredParameters();

    public String getEventLabel(){
        return eventLabel;
    };
}
