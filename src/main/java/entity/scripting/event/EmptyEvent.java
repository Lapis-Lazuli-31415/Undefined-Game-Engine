package entity.scripting.event;

public class EmptyEvent extends Event{
    private static String EMPTY_EVENT_TYPE = "Empty";

    public EmptyEvent() {
        super("Empty");
    }

    public static String getEventType() {
        return EMPTY_EVENT_TYPE;
    }
}
