package entity.scripting;

import java.util.List;
import java.util.ArrayList;

public class TriggerManager {
    private List<Trigger> triggers;

    public TriggerManager() {
        triggers = new ArrayList<>();
    }

    // TODO: Delete this method
    public List<Trigger> getAllTriggers() {
        return triggers;
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public Trigger getTrigger(int index){
        return triggers.get(index);
    }

    public void addTrigger(Trigger trigger){
        triggers.add(trigger);
    }

    public void deleteTrigger(int index){
        triggers.remove(index);
    }
}
