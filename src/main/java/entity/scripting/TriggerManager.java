package entity.scripting;

import java.util.List;
import java.util.ArrayList;

public class TriggerManager {
    private List<Trigger> triggers;

    public TriggerManager() {
        triggers = new ArrayList<>();
    }

    public List<Trigger> getAllTriggers() {
        return triggers;
    }
}
