package entity.scripting;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.ArrayList;

public class TriggerManager {
    private List<Trigger> triggers;

    public TriggerManager() {
        triggers = new ArrayList<>();
    }

    @JsonCreator
    public TriggerManager(@JsonProperty("triggers") List<Trigger> triggers) {
        this.triggers = triggers != null ? triggers : new ArrayList<>();
    }

    // @JsonIgnore tells Jackson to skip this method when saving
    @JsonIgnore
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
    // --- Copy method for preview isolation ---

    /**
     * Create a deep copy of this TriggerManager.
     *
     * @return A new TriggerManager with copied triggers
     */
    public TriggerManager copy() {
        TriggerManager copy = new TriggerManager();
        for (Trigger trigger : this.triggers) {
            copy.addTrigger(trigger.copy());
        }
        return copy;
    }
}
