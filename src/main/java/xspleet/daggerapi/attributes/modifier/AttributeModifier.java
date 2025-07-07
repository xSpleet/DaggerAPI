package xspleet.daggerapi.attributes.modifier;

import xspleet.daggerapi.attributes.operations.AttributeOperation;

import java.util.UUID;

public interface AttributeModifier<T>
{
    public T getValue();
    public AttributeOperation<T> getOperation();
    public UUID getUUID();
}
