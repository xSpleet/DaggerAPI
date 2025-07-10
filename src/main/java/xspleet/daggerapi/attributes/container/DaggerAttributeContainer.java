package xspleet.daggerapi.attributes.container;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.instance.AttributeInstance;
import xspleet.daggerapi.attributes.instance.DaggerAttributeInstance;
import xspleet.daggerapi.attributes.modifier.AttributeModifier;
import xspleet.daggerapi.collections.registration.Mapper;
import xspleet.daggerapi.exceptions.DaggerAPIException;
import xspleet.daggerapi.exceptions.NoSuchAttributeException;

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

    public PacketByteBuf toPacketByteBuf()
    {
        PacketByteBuf buf = PacketByteBufs.create();

        var instancesToUpdate = attributeInstances.entrySet()
            .stream()
            .filter((e) -> e.getValue().isDirty())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        buf.writeMap(
                instancesToUpdate,
                (byteBuf, attribute) -> byteBuf.writeString(Mapper.getNameOf(attribute)),
                (byteBuf, instance) -> instance.write(byteBuf)
        );
        return buf;
    }

    public void acceptSyncContainer(SyncContainer container)
    {
        for(var entry : container.getSyncData().entrySet())
        {
            var attribute = entry.getKey();
            var instanceSyncData = entry.getValue();
            if(instanceSyncData == null) continue;

            if(!attributeInstances.containsKey(attribute))
            {
                throw new IllegalArgumentException("Attribute " + attribute.getName() + " is not registered.");
            }

            var attributeInstance = attributeInstances.get(attribute);
            for(var modifier : instanceSyncData.addedModifiers())
            {
                ((AttributeInstance<Object>)attributeInstance).addTemporaryModifier((AttributeModifier<Object>) modifier);
            }
            for(var modifierId : instanceSyncData.removedModifiers())
            {
                attributeInstance.removeModifier(modifierId);
            }
        }
    }

    public static SyncContainer readFromPacket(PacketByteBuf buf)
    {
            return new SyncContainer(buf.readMap(
                    byteBuf -> {
                        try {
                            return Mapper.getAttribute(byteBuf.readString(32767));
                        } catch (NoSuchAttributeException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    DaggerAttributeInstance::read
            ));
    }

    public boolean needsSync()
    {
        return attributeInstances.values().stream().anyMatch(AttributeInstance::isDirty);
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
