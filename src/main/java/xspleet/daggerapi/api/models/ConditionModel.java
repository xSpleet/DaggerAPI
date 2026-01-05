package xspleet.daggerapi.api.models;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

public class ConditionModel extends JSONModel
{
    private String condition;
    private Map<String, JsonElement> arguments = new HashMap<>();
    private String on;

    public String getCondition() {
        return condition;
    }

    public Map<String, JsonElement> getArguments() {
        return arguments;
    }

    public String getOn() {
        return on;
    }

}
