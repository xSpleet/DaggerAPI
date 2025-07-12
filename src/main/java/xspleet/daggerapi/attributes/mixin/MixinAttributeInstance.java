package xspleet.daggerapi.attributes.mixin;

import net.minecraft.network.PacketByteBuf;
import xspleet.daggerapi.attributes.instance.AttributeInstance;
import xspleet.daggerapi.attributes.modifier.AttributeModifier;

import java.util.List;
import java.util.UUID;

public interface MixinAttributeInstance<T> extends AttributeInstance<T>
{
    public String DaggerAPI$getAttributeName();
    public boolean DaggerAPI$hasModifier(AttributeModifier<T> modifier);
    public boolean DaggerAPI$hasModifier(java.util.UUID modifierId);
    public void DaggerAPI$addTemporaryModifier(AttributeModifier<T> modifier);
    public void DaggerAPI$removeModifier(AttributeModifier<T> modifier);
    public T DaggerAPI$getValue();
    public T DaggerAPI$getBaseValue();
    public void DaggerAPI$removeModifier(UUID modifierId);
    public void DaggerAPI$write(PacketByteBuf byteBuf);
    public List<AttributeModifier<T>> DaggerAPI$getModifiers();

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

    public default void write(PacketByteBuf byteBuf) {
        DaggerAPI$write(byteBuf);
    }

    public default void removeModifier(UUID modifierId) {
        DaggerAPI$removeModifier(modifierId);
    }

    public default List<AttributeModifier<T>> getModifiers() {
        return DaggerAPI$getModifiers();
    }
}
