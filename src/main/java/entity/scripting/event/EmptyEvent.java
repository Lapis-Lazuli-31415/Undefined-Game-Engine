package entity.scripting.event;

public class EmptyEvent extends Event{
    private static final String EVENT_TYPE = "Empty";

    public EmptyEvent() {
        super("Empty");
    }

    public static String getEventType() {
        return EVENT_TYPE;
    }
}
