package xspleet.daggerapi.attributes.instance;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.network.PacketByteBuf;
import xspleet.daggerapi.attributes.modifier.AttributeModifier;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.modifier.DaggerAttributeModifier;
import xspleet.daggerapi.attributes.operations.AttributeOperation;
import xspleet.daggerapi.collections.registration.Mapper;

import java.util.*;

public class DaggerAttributeInstance<T> implements AttributeInstance<T>
{
    private final TreeMap<AttributeOperation<T>, Set<AttributeModifier<T>>> temporaryModifiers = new TreeMap<>(Comparator.comparingInt(AttributeOperation::getPrecedence));
    private final Set<AttributeModifier<T>> allModifiers = new HashSet<>();
    private final Attribute<T> attribute;

    private final List<UUID> removedModifiers = new ArrayList<>();
    private final List<AttributeModifier<T>> addedModifiers = new ArrayList<>();

    private boolean updated = true;
    private T value;

    public DaggerAttributeInstance(Attribute<T> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getAttributeName() {
        return attribute.getName();
    }

    @Override
    public boolean hasModifier(AttributeModifier<T> modifier) {
        return allModifiers.contains(modifier);
    }

    @Override
    public boolean hasModifier(UUID modifierId) {
        return allModifiers.stream().anyMatch(x -> x.getUUID().equals(modifierId));
    }

    @Override
    public void addTemporaryModifier(AttributeModifier<T> modifier) {
        temporaryModifiers.putIfAbsent(modifier.getOperation(), new HashSet<>());
        temporaryModifiers.get(modifier.getOperation()).add(modifier);
        allModifiers.add(modifier);
        if(attribute.isTracked())
            addedModifiers.add(modifier);
        updated = true;
    }

    @Override
    public void removeModifier(AttributeModifier<T> modifier) {
        if (allModifiers.remove(modifier)) {
            Set<AttributeModifier<T>> modifiers = temporaryModifiers.get(modifier.getOperation());
            if (modifiers != null) {
                modifiers.remove(modifier);
                if (modifiers.isEmpty()) {
                    temporaryModifiers.remove(modifier.getOperation());
                }
            }
        }
        if(attribute.isTracked())
        {
            if(addedModifiers.contains(modifier))
                addedModifiers.remove(modifier);
            else
                removedModifiers.add(modifier.getUUID());
        }
        updated = true;
    }

    @Override
    public void removeModifier(UUID modifierId) {
        allModifiers.stream()
                .filter(m -> m.getUUID().equals(modifierId))
                .findFirst().ifPresent(this::removeModifier);
        updated = true;
    }

    @Override
    public T getValue() {
        if(!updated)
            return value;
        T computedValue = attribute.getDefaultValue();
        for(AttributeOperation<T> operation : temporaryModifiers.keySet()) {
            Set<AttributeModifier<T>> modifiers = temporaryModifiers.getOrDefault(operation, Collections.emptySet());
            T valuePreviousGroup = computedValue;
            for (AttributeModifier<T> modifier : modifiers) {
                computedValue = operation.apply(computedValue, valuePreviousGroup, modifier.getValue());
            }
        }
        updated = false;
        value = computedValue;
        return attribute.clamp(computedValue);
    }

    @Override
    public T getBaseValue() {
        return attribute.getDefaultValue();
    }

    @Override
    public boolean isDirty() {
        return attribute.isTracked() && (!removedModifiers.isEmpty() || !addedModifiers.isEmpty());
    }

    @Override
    public void write(PacketByteBuf buf) {

        buf.writeCollection(
                removedModifiers,
                PacketByteBuf::writeUuid
        );

        buf.writeCollection(
                addedModifiers,
                (b, modifier) -> modifier.write(b)
        );

        removedModifiers.clear();
        addedModifiers.clear();
    }

    public static AttributeInstanceSyncData read(PacketByteBuf buf) {
        List<UUID> removedModifiers = buf.readCollection(
                ArrayList::new,
                PacketByteBuf::readUuid
        );

        List<AttributeModifier<?>> addedModifiers = buf.readCollection(
                ArrayList::new,
                (b) -> {
                    UUID uuid = b.readUuid();
                    String name = b.readString(32767);
                    String operationName = b.readString(32767);
                    AttributeOperation<?> operation = Mapper.getOperation(operationName);
                    return readModifier(
                            b, uuid, name, operation
                    );
                }
        );

        return new AttributeInstanceSyncData(removedModifiers, addedModifiers);
    }

    private static <T> AttributeModifier<T> readModifier(PacketByteBuf buf, UUID uuid, String name, AttributeOperation<T> operation)
    {
        T value;
        if(operation.getType() == Double.class) {
            value = operation.getType().cast(buf.readDouble());
        } else if(operation.getType() == Integer.class) {
            value = operation.getType().cast(buf.readInt());
        } else if(operation.getType() == Boolean.class) {
            value = operation.getType().cast(buf.readBoolean());
        } else {
            throw new IllegalArgumentException("Unsupported attribute modifier type: " + operation.getType().getName());
        }
        return new DaggerAttributeModifier<>(uuid, name, value, operation);
    }
}
