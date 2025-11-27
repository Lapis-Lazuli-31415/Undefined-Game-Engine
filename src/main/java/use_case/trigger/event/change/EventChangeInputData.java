package use_case.trigger.event.change;

public class EventChangeInputData {
    private final int index;
    private final String event;

    public EventChangeInputData(int index, String event){
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
