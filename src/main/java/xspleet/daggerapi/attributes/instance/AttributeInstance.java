package xspleet.daggerapi.attributes.instance;

import xspleet.daggerapi.attributes.modifier.AttributeModifier;

public interface AttributeInstance<T>
{
    public boolean hasModifier(AttributeModifier<T> modifier);
    public void addTemporaryModifier(AttributeModifier<T> modifier);
    public void removeModifier(AttributeModifier<T> modifier);
    public T getValue();
    public T getBaseValue();
}
