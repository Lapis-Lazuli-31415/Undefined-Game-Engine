package entity.scripting;

import java.util.ArrayList;

public class TriggerManager {
    private ArrayList<Trigger> triggers;

    public TriggerManager() {
        triggers = new ArrayList<>();
    }

    public ArrayList<Trigger> getAllTriggers() {
        return triggers;
    }
}
