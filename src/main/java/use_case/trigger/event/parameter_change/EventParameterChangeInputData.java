package use_case.trigger.event.parameter_change;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EventParameterChangeInputData {
    private int index;
    private String parameterName;
    private String parameterValue;

    public EventParameterChangeInputData(int index, String parameterName, String parameterValue) {
        this.index = index;
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public int getIndex() {
        return index;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterValue() {
        return parameterValue;
    }
}
