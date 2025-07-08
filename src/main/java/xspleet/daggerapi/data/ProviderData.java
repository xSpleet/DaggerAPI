package xspleet.daggerapi.data;

import org.apache.commons.lang3.NotImplementedException;
import xspleet.daggerapi.data.key.DaggerKey;
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

    public On getOn() {
        return on;
    }

    public ProviderData setOn(On on) {
        this.on = on;
        return this;
    }

    @Override
    public <T> DaggerContext addData(DaggerKey<T> key, T value) {
        throw new NotImplementedException();
    }

    @Override
    public <T> T getData(DaggerKey<T> key) {
        throw new NotImplementedException();
    }

    @Override
    public boolean hasData(DaggerKey<?> key) {
        return false;
    }

    public ProviderData addData(String key, String value) {
        arguments.put(key, value);
        return this;
    }

    public String getData(String key) {
        return arguments.get(key);
    }
}
