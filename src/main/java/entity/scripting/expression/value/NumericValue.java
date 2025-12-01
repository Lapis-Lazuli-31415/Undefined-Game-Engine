package entity.scripting.expression.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import entity.scripting.environment.Environment;
import entity.scripting.error.ParseSyntaxException;
import entity.scripting.expression.NumericExpression;

public class NumericValue implements NumericExpression {
    private final double value;
    public static final String KEY_WORD = "number";

    // Tells Jackson how to construct this object from JSON
    @JsonCreator
    public NumericValue(@JsonProperty("value") double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public Double evaluate(Environment globalEnvironment, Environment localEnvironment) {
        return value;
    }

    public static NumericValue parse(String string) throws ParseSyntaxException {
        if (isNumeric(string)) {
            return new NumericValue(Double.parseDouble(string));
        } else {
            throw new ParseSyntaxException("Invalid Syntax: " +  string + " is not a numeric value");
        }
    }

    private static boolean isNumeric(String string) {
        if (string == null) {
            return false; // Handle null strings
        }
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String format() {
        return KEY_WORD + ":(" + value + ")";
    }
}
