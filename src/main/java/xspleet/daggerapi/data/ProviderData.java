package xspleet.daggerapi.data;

import org.jetbrains.annotations.Nullable;
import xspleet.daggerapi.models.On;

import java.util.HashMap;
import java.util.Map;

public class ProviderData implements DaggerContext
{
    private Map<String, String> arguments = new HashMap<>();
    private On on = On.WORLD;


    public ProviderData(Map<String, String> arguments) {
        this.arguments = arguments;
    }

    public ProviderData() {
    }

    @Override
    public ProviderData addData(String key, String value) {
        arguments.put(key, value);
        return this;
    }

    @Override
    public String getData(String key) {
        return arguments.get(key);
    }

    public On getOn() {
        return on;
    }

    public ProviderData setOn(On on) {
        this.on = on;
        return this;
    }
}
