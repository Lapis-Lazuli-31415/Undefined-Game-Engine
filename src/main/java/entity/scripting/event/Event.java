package entity.scripting.event;

public abstract class Event {
    private final String eventLabel;

    public Event(String eventLabel) {
        this.eventLabel = eventLabel;
    }

    public String getEventLabel(){
        return eventLabel;
    };
}
