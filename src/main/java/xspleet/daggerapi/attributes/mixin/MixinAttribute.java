package xspleet.daggerapi.attributes.mixin;

import xspleet.daggerapi.attributes.Attribute;

public interface MixinAttribute<T> extends Attribute<T>
{
    String DaggerAPI$getName();
    T DaggerAPI$getDefaultValue();
    Class<T> DaggerAPI$getType();
    T DaggerAPI$clamp(T value);

    default MixinAttribute<T> DaggerAPI$setTracked() {
        return this;
    }

    default boolean DaggerAPI$isTracked(){
        return false;
    }

    default String getName() {
        return DaggerAPI$getName();
    }

    default T getDefaultValue() {
        return DaggerAPI$getDefaultValue();
    }

    default Class<T> getType() {
        return DaggerAPI$getType();
    }

    default T clamp(T value) {
        return DaggerAPI$clamp(value);
    }

    default MixinAttribute<T> setUntracked() {
        return DaggerAPI$setTracked();
    }

    default boolean isTracked() {
        return DaggerAPI$isTracked();
    }
}
