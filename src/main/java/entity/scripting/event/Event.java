package entity.scripting.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// TYPE INFO
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
// REGISTER EVENT SUBCLASSES
@JsonSubTypes({
        @JsonSubTypes.Type(value = OnClickEvent.class, name = "OnClick"),
        @JsonSubTypes.Type(value = OnKeyPressEvent.class, name = "OnKeyPress"),
        @JsonSubTypes.Type(value = EmptyEvent.class, name = "Empty")
})
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