package xspleet.daggerapi.data;

import org.apache.commons.lang3.NotImplementedException;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.models.On;

import java.util.HashMap;
import java.util.Map;

public class ProviderData implements DaggerContext
{
    private Map<DaggerKey<?>, Object> arguments = new HashMap<>();
    private On on = On.WORLD;

    public ProviderData() {
        this.arguments = new HashMap<>();
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
        arguments.put(key, value);
        return this;
    }

    @Override
    public <T> T getData(DaggerKey<T> key) {
        return key.type().cast(arguments.get(key));
    }

    @Override
    public boolean hasData(DaggerKey<?> key) {
        return arguments.containsKey(key);
    }
}
