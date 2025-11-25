package entity.scripting.event;

public class OnClickEvent extends Event{
    public static final String EVENT_TYPE = "On Click";

    public OnClickEvent() {
        super(EVENT_TYPE);
    }

    public static String getEventType() {
        return EVENT_TYPE;
    }

    @Override
    public boolean isRequiredParameter(String key){
        return false;
    }
}
