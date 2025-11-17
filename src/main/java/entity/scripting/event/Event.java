package entity.scripting.event;

public abstract class Event {
    private String eventLabel;

    public Event(String eventLabel) {
        this.eventLabel = eventLabel;
    }

    public String getEventLabel(){
        return eventLabel;
    };
}
