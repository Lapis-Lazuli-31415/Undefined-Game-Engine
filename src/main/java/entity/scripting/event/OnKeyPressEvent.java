package entity.scripting.event;

public class OnKeyPressEvent extends Event{
    private static String EVENT_TYPE = "On Key Press";
    private String key;

    public OnKeyPressEvent(String key) {
        super("On Key Press");
        this.key = key;
    }

    public static String getEventType() {
        return EVENT_TYPE;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    @Override
    public String toString() {
        return "OnKeyPressEvent[key=" + key + "]";
    }
}
