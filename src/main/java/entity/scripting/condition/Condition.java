package entity.scripting.condition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import entity.scripting.environment.Environment;
import entity.scripting.error.ParseSyntaxException;

// Add Type Info
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
// Register Subclasses
@JsonSubTypes({
        @JsonSubTypes.Type(value = NumericComparisonCondition.class, name = "NumericComparison"),
        @JsonSubTypes.Type(value = BooleanComparisonCondition.class, name = "BooleanComparison"),
        @JsonSubTypes.Type(value = EmptyCondition.class, name = "Empty")
})
public abstract class Condition {
    public abstract boolean evaluate(Environment globalEnvironment, Environment localEnvironment) throws Exception;
    public abstract void parse(String string) throws ParseSyntaxException;
    public abstract String format();
    public abstract String getConditionType();
}