package xspleet.daggerapi.attributes.mixin;

import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.attributes.instance.AttributeInstance;

public interface MixinAttributeHolder extends AttributeHolder
{
    public <T> AttributeInstance<T> DaggerAPI$getAttributeInstance(Attribute<T> attribute);

    public default <T> AttributeInstance<T> getAttributeInstance(Attribute<T> attribute) {
        return DaggerAPI$getAttributeInstance(attribute);
    }
}


