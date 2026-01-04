package xspleet.daggerapi.data.collection;

import xspleet.daggerapi.api.models.OnModel;
import xspleet.daggerapi.data.ComplexDataEntry;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.api.collections.DaggerKeys;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProviderData implements DaggerContext
{
    private final Map<DaggerKey<?>, Object> arguments;
    private OnModel on = OnModel.WORLD;

    public ProviderData() {
        this.arguments = new HashMap<>();
    }

    public OnModel getOn() {
        return on;
    }

    public ProviderData setOn(OnModel on) {
        this.on = on;
        return this;
    }

    @Override
    public <T> ProviderData addData(DaggerKey<T> key, T value) {
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

    public Set<DaggerKey<?>> getRequiredKeys() {
        Set<DaggerKey<?>> requiredData = new HashSet<>();
        for(var entry : arguments.entrySet()) {
            if(entry.getValue() instanceof ComplexDataEntry complexDataEntry) {
                requiredData.addAll(complexDataEntry.getRequiredData());
            }
        }
        switch(on) {
            case SELF -> requiredData.add(DaggerKeys.PLAYER);
            case SOURCE -> requiredData.add(DaggerKeys.TRIGGER_SOURCE);
            case TRIGGERED -> requiredData.add(DaggerKeys.TRIGGERED);
            case WORLD -> requiredData.add(DaggerKeys.WORLD);
        }
        return requiredData;
    }
}
