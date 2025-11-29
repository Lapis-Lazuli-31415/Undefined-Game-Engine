package entity.scripting.action;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import entity.scripting.environment.Environment;

// Add Type Info
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
// Register Subclasses
@JsonSubTypes({
        @JsonSubTypes.Type(value = WaitAction.class, name = "WaitAction"),
        @JsonSubTypes.Type(value = NumericVariableAssignmentAction.class, name = "AssignNumeric"),
        @JsonSubTypes.Type(value = BooleanVariableAssignmentAction.class, name = "AssignBoolean"),
        @JsonSubTypes.Type(value = EmptyAction.class, name = "Empty")
})
public abstract class Action {
    public abstract void execute(Environment globalEnvironment, Environment localEnvironment) throws Exception;
}