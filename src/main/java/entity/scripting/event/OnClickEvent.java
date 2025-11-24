package entity.scripting.event;

public class OnClickEvent extends Event{
    private static final String EVENT_TYPE = "On Click";

    public OnClickEvent() {
        super("On Click");
    }

    public static String getEventType() {
        return EVENT_TYPE;
    }
}
