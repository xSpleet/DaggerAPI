package xspleet.daggerapi.attributes.mixin;

import xspleet.daggerapi.attributes.instance.AttributeInstance;
import xspleet.daggerapi.attributes.modifier.AttributeModifier;

public interface MixinAttributeInstance<T> extends AttributeInstance<T>
{
    public String DaggerAPI$getAttributeName();
    public boolean DaggerAPI$hasModifier(AttributeModifier<T> modifier);
    public boolean DaggerAPI$hasModifier(java.util.UUID modifierId);
    public void DaggerAPI$addTemporaryModifier(AttributeModifier<T> modifier);
    public void DaggerAPI$removeModifier(AttributeModifier<T> modifier);
    public T DaggerAPI$getValue();
    public T DaggerAPI$getBaseValue();
    public void DaggerAPI$clean();
    public boolean DaggerAPI$isDirty();

    public default String getAttributeName() {
        return DaggerAPI$getAttributeName();
    }

    public default boolean hasModifier(AttributeModifier<T> modifier) {
        return DaggerAPI$hasModifier(modifier);
    }

    public default boolean hasModifier(java.util.UUID modifierId) {
        return DaggerAPI$hasModifier(modifierId);
    }

    public default void addTemporaryModifier(AttributeModifier<T> modifier) {
        DaggerAPI$addTemporaryModifier(modifier);
    }

    public default void removeModifier(AttributeModifier<T> modifier) {
        DaggerAPI$removeModifier(modifier);
    }

    public default T getValue() {
        return DaggerAPI$getValue();
    }

    public default T getBaseValue() {
        return DaggerAPI$getBaseValue();
    }

    public default void clean() {
        DaggerAPI$clean();
    }

    public default boolean isDirty() {
        return DaggerAPI$isDirty();
    }
}
