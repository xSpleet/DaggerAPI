package xspleet.daggerapi.attributes.container;

import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.instance.AttributeInstance;
import xspleet.daggerapi.attributes.instance.DaggerAttributeInstance;
import xspleet.daggerapi.attributes.modifier.AttributeModifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class DaggerAttributeContainer
{
    protected final HashMap<Attribute<?>, AttributeInstance<?>> attributeInstances = new HashMap<>();

    protected DaggerAttributeContainer(HashMap<Attribute<?>, AttributeInstance<?>> attributeInstances)
    {
        this.attributeInstances.putAll(attributeInstances);
    }

    private <T> AttributeInstance<T> getInstance(Attribute<T> attribute)
    {
        if(!attributeInstances.containsKey(attribute))
        {
            var instance = new DaggerAttributeInstance<T>(attribute);
            attributeInstances.put(attribute, instance);
            return instance;
        }
        return (AttributeInstance<T>) attributeInstances.get(attribute);
    }

    public void acceptSyncContainer(SyncAttributeContainer syncContainer) {
        for (Map.Entry<Attribute<?>, AttributeInstance<?>> entry : syncContainer.attributeInstances.entrySet()) {
            var attribute = entry.getKey();
            var instance = getInstance(attribute);
            if(!attributeInstances.containsKey(attribute))
                throw new IllegalStateException("Attribute " + attribute.getName() + " is not present in this container.");
            attributeInstances.put(attribute, instance);
        }
    }

    public SyncAttributeContainer getSyncContainer() {
        var syncData = attributeInstances.entrySet()
                .stream()
                .filter((e) -> e.getValue().isDirty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new SyncAttributeContainer(new HashMap<>(syncData));
    }

    private <T> AttributeInstance<T> require(Attribute<T> attribute)
    {
        if(!attributeInstances.containsKey(attribute))
            throw new IllegalArgumentException("Attribute " + attribute.getName() + " is not present in this container.");
        return getInstance(attribute);
    }

    public <T> AttributeInstance<T> getAttributeInstance(Attribute<T> attribute)
    {
        return require(attribute);
    }

    public <T> boolean hasModifier(Attribute<T> attribute, AttributeModifier<T> modifier)
    {
        return require(attribute).hasModifier(modifier);
    }

    public <T> boolean hasModifier(Attribute<T> attribute, UUID modifierId) {
        return require(attribute).hasModifier(modifierId);
    }

    public <T> void addTemporaryModifier(Attribute<T> attribute, AttributeModifier<T> modifier)
    {
        require(attribute).addTemporaryModifier(modifier);
    }

    public <T> void removeModifier(Attribute<T> attribute, AttributeModifier<T> modifier)
    {
        require(attribute).removeModifier(modifier);
    }

    public <T> T getValue(Attribute<T> attribute)
    {
        return require(attribute).getValue();
    }

    public static class Builder{
        private final DaggerAttributeContainer container;

        public Builder() {
            this.container = new DaggerAttributeContainer(new HashMap<>());
        }

        public <T> Builder addAttribute(Attribute<T> attribute) {
            container.getInstance(attribute);
            return this;
        }

        public DaggerAttributeContainer build() {
            return container;
        }
    }
}
