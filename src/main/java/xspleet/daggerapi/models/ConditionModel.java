package xspleet.daggerapi.models;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

public class ConditionModel extends JSONModel
{
    private String condition;
    private Map<String, JsonElement> arguments = new HashMap<>();
    private On on = On.SELF;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Map<String, JsonElement> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, JsonElement> arguments) {
        this.arguments = arguments;
    }

    public On getOn() {
        return on;
    }

    public void setOn(On on) {
        this.on = on;
    }
}
