package entity.scripting.event;

public interface EventFactory {
    Event create(String type);
    String[] getRegisteredEvents();
}
