package entity.scripting.expression;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import entity.scripting.error.ParseSyntaxException;
import entity.scripting.expression.value.BooleanValue;
import entity.scripting.expression.variable.BooleanVariable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BooleanValue.class, name = "BooleanValue"),
        @JsonSubTypes.Type(value = BooleanVariable.class, name = "BooleanVariable"),
        @JsonSubTypes.Type(value = AndExpression.class, name = "And"),
        @JsonSubTypes.Type(value = OrExpression.class, name = "Or"),
        @JsonSubTypes.Type(value = NotExpression.class, name = "Not")
})
public interface BooleanExpression extends Expression<Boolean>{
}
