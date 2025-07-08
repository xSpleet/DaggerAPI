package xspleet.daggerapi.attributes.modifier;

import net.minecraft.network.PacketByteBuf;
import xspleet.daggerapi.attributes.operations.AttributeOperation;

import java.util.UUID;

public class DaggerAttributeModifier<T> implements AttributeModifier<T>
{
    private final T value;
    private final String name;
    private final AttributeOperation<T> operation;
    private final UUID uuid;

    public DaggerAttributeModifier(UUID uuid, String name, T value, AttributeOperation<T> operation) {
        this.value = value;
        this.name = name;
        this.operation = operation;
        this.uuid = uuid;
    }

    public DaggerAttributeModifier(String name, T value, AttributeOperation<T> operation) {
        this(UUID.randomUUID(), name, value, operation);
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public AttributeOperation<T> getOperation() {
        return operation;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }
}
