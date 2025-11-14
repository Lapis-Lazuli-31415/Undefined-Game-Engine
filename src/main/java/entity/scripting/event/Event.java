package entity.scripting.event;

public abstract class Event {
    private String eventName;

    public Event(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName(){
        return eventName;
    };
}
