package entity.scripting.event;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultEventFactory implements EventFactory{
    private final Map<String, Supplier<Event>> registry = new LinkedHashMap<>();

    public DefaultEventFactory() {
        registry.put(EmptyEvent.EVENT_TYPE, EmptyEvent::new);
        registry.put(OnClickEvent.EVENT_TYPE, OnClickEvent::new);
        registry.put(OnKeyPressEvent.EVENT_TYPE, OnKeyPressEvent::new);
    }

    @Override
    public Event create(String type) {
        Supplier<Event> supplier = registry.get(type);
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown event type: " + type);
        }
        return supplier.get();
    }

    @Override
    public List<String> getRegisteredEvents() {
        return List.copyOf(registry.keySet());
    }
}
