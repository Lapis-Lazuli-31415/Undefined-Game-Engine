package entity.scripting.expression;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import entity.scripting.expression.value.NumericValue;
import entity.scripting.expression.variable.NumericVariable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NumericValue.class, name = "NumericValue"),
        @JsonSubTypes.Type(value = SimpleArithmeticOperation.class, name = "Arithmetic"),
        @JsonSubTypes.Type(value = NumericVariable.class, name = "NumericVariable")
})
public interface NumericExpression extends Expression<Double>{
}