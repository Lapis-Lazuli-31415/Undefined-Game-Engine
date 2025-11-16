package entity.scripting.event;

public class OnClickEvent extends Event{
    private static String ON_CLICK_EVENT_TYPE = "On Click";

    public OnClickEvent() {
        super("On Click");
    }

    public static String getEventType() {
        return ON_CLICK_EVENT_TYPE;
    }
}
