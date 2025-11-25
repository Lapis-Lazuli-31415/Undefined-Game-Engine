package entity.scripting.event;

import java.util.List;

public interface EventFactory {
    Event create(String type);
    List<String> getRegisteredEvents();
}
