package xspleet.daggerapi.attributes.mixin;

import xspleet.daggerapi.attributes.Attribute;

public interface MixinAttribute<T> extends Attribute<T>
{
    public String DaggerAPI$getName();
    public T DaggerAPI$getDefaultValue();
    public Class<T> DaggerAPI$getType();
    public T DaggerAPI$clamp(T value);

    public default MixinAttribute<T> DaggerAPI$setTracked() {
        return this;
    }

    public default boolean DaggerAPI$isTracked(){
        return false;
    }

    public default String getName() {
        return DaggerAPI$getName();
    }

    public default T getDefaultValue() {
        return DaggerAPI$getDefaultValue();
    }

    public default Class<T> getType() {
        return DaggerAPI$getType();
    }

    public default T clamp(T value) {
        return DaggerAPI$clamp(value);
    }

    public default MixinAttribute<T> setTracked() {
        return DaggerAPI$setTracked();
    }

    public default boolean isTracked() {
        return DaggerAPI$isTracked();
    }
}
