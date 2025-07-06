package xspleet.daggerapi.attributes.base;

import java.util.UUID;

public interface AttributeModifier<T>
{
    public T getValue();
    public AttributeOperation<T> getOperation();
    public UUID getUUID();
}
