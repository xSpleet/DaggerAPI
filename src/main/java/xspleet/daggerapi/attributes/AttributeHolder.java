package xspleet.daggerapi.attributes;

import xspleet.daggerapi.attributes.instance.AttributeInstance;

public interface AttributeHolder
{
    public <T> AttributeInstance<T> getAttributeInstance(Attribute<T> attribute);
}
