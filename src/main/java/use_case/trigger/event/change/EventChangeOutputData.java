package use_case.trigger.event.change;

public class EventChangeOutputData {
    private int index;
    private final String event;

    public EventChangeOutputData(int index, String event){
        this.index = index;
        this.event = event;
    }

    public int getIndex() {
        return index;
    }

    public String getEvent() {
        return event;
    }
}
