package xspleet.daggerapi.attributes;

import xspleet.daggerapi.attributes.base.AttributeModifier;
import xspleet.daggerapi.attributes.base.AttributeOperation;

import java.util.UUID;

public class DaggerAttributeModifier<T> implements AttributeModifier<T>
{
    private final T value;
    private final AttributeOperation<T> operation;
    private final UUID uuid;

    public DaggerAttributeModifier(UUID uuid, T value, AttributeOperation<T> operation) {
        this.value = value;
        this.operation = operation;
        this.uuid = uuid;
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
}
