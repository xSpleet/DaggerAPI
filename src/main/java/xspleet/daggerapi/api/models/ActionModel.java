package xspleet.daggerapi.api.models;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

public class ActionModel extends JSONModel
{
    private String action;
    private Map<String, JsonElement> arguments = new HashMap<>();
    private String on;
    private String repeat = "1";

    public String getAction() {
        return action;
    }

    public Map<String, JsonElement> getArguments() {
        return arguments;
    }

    public String getOn() {
        return on;
    }

    public String getRepeat() {
        return repeat;
    }
}
