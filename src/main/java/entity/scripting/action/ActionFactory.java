package entity.scripting.action;

import java.util.List;

public interface ActionFactory {
    Action create(String type);
    List<String> getRegisteredActions();
}
