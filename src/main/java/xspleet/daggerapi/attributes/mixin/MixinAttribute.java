package xspleet.daggerapi.attributes.mixin;

import xspleet.daggerapi.attributes.Attribute;

public interface MixinAttribute<T> extends Attribute<T>
{
    public String DaggerAPI$getName();
    public T DaggerAPI$getDefaultValue();
    public T DaggerAPI$clamp(T value);

    public default String getName() {
        return DaggerAPI$getName();
    }

    public default T getDefaultValue() {
        return DaggerAPI$getDefaultValue();
    }

    public default T clamp(T value) {
        return DaggerAPI$clamp(value);
    }
}
