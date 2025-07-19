package xspleet.daggerapi.attributes.mixin;

import net.minecraft.network.PacketByteBuf;
import xspleet.daggerapi.attributes.instance.AttributeInstance;
import xspleet.daggerapi.attributes.modifier.AttributeModifier;

import java.util.List;
import java.util.UUID;

public interface MixinAttributeInstance<T> extends AttributeInstance<T>
{
    String DaggerAPI$getAttributeName();
    boolean DaggerAPI$hasModifier(AttributeModifier<T> modifier);
    boolean DaggerAPI$hasModifier(java.util.UUID modifierId);
    void DaggerAPI$addTemporaryModifier(AttributeModifier<T> modifier);
    void DaggerAPI$removeModifier(AttributeModifier<T> modifier);
    T DaggerAPI$getValue();
    T DaggerAPI$getBaseValue();
    void DaggerAPI$removeModifier(UUID modifierId);
    void DaggerAPI$write(PacketByteBuf byteBuf);
    List<AttributeModifier<T>> DaggerAPI$getModifiers();

    default String getAttributeName() {
        return DaggerAPI$getAttributeName();
    }

    default boolean hasModifier(AttributeModifier<T> modifier) {
        return DaggerAPI$hasModifier(modifier);
    }

    default boolean hasModifier(java.util.UUID modifierId) {
        return DaggerAPI$hasModifier(modifierId);
    }

    default void addTemporaryModifier(AttributeModifier<T> modifier) {
        DaggerAPI$addTemporaryModifier(modifier);
    }

    default void removeModifier(AttributeModifier<T> modifier) {
        DaggerAPI$removeModifier(modifier);
    }

    default T getValue() {
        return DaggerAPI$getValue();
    }

    default T getBaseValue() {
        return DaggerAPI$getBaseValue();
    }

    default void write(PacketByteBuf byteBuf) {
        DaggerAPI$write(byteBuf);
    }

    default void removeModifier(UUID modifierId) {
        DaggerAPI$removeModifier(modifierId);
    }

    default List<AttributeModifier<T>> getModifiers() {
        return DaggerAPI$getModifiers();
    }
}
