package xspleet.daggerapi.attributes.container;

import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.instance.AttributeInstance;
import xspleet.daggerapi.attributes.instance.DaggerAttributeInstance;

import java.util.HashMap;

public class DaggerAttributeContainer
{
    private final HashMap<Attribute, AttributeInstance> attributeInstances = new HashMap<>();
    private final HashMap<Attribute, Boolean> attributeDirtyFlags = new HashMap<>();

    protected DaggerAttributeContainer()
    {

    }

    public <T> AttributeInstance<T> getAttributeInstance(Attribute<T> attribute)
    {
        if(!attributeInstances.containsKey(attribute))
            attributeDirtyFlags.put(attribute, true);
        return (DaggerAttributeInstance<T>) attributeInstances.computeIfAbsent(attribute, k -> new DaggerAttributeInstance<>(attribute));
    }

    public SyncAttributeContainer getSyncContainer()
    {
        attributeInstances.entrySet()
                .stream()
                .filter(e -> attributeDirtyFlags.getOrDefault(e.getKey(), false))

    }
}
