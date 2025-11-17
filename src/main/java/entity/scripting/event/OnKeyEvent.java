package entity.scripting.event;

public class OnKeyEvent extends Event{
    private static String ON_CLICK_EVENT_TYPE = "On Key";
    private String key;

    public OnKeyEvent(String key) {
        super("On Key");
        this.key = key;
    }

    public static String getEventType() {
        return ON_CLICK_EVENT_TYPE;
    }

    public String getKey() {
        return key;
    }
}
