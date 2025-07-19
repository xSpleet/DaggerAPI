package xspleet.daggerapi.api.models;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

public class ActionModel extends JSONModel
{
    private String action;
    private Map<String, JsonElement> arguments = new HashMap<>();
    private On on = On.TRIGGERED;

    public String getAction() {
        return action;
    }

    public Map<String, JsonElement> getArguments() {
        return arguments;
    }

    public On getOn() {
        return on;
    }

}
