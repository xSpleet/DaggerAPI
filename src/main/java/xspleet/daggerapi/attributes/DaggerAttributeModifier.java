package xspleet.daggerapi.attributes;

import xspleet.daggerapi.attributes.operations.DaggerOperation;

public interface DaggerAttributeModifier<T>
{
    public T getValue();
    public DaggerOperation<T> getOperation();
}
