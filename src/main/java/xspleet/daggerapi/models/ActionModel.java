package xspleet.daggerapi.models;

import java.util.HashMap;
import java.util.Map;

public class ActionModel extends JSONModel
{
    private String action;
    private Map<String, String> arguments = new HashMap<>();
    private On on = On.TRIGGERED;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, String> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, String> arguments) {
        this.arguments = arguments;
    }

    public On getOn() {
        return on;
    }

    public void setOn(On on) {
        this.on = on;
    }
}
