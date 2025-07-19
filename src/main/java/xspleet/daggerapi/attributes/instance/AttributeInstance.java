package xspleet.daggerapi.attributes.instance;

import net.minecraft.network.PacketByteBuf;
import xspleet.daggerapi.attributes.modifier.AttributeModifier;

import java.util.List;
import java.util.UUID;

public interface AttributeInstance<T>
{
    String getAttributeName();
    boolean hasModifier(AttributeModifier<T> modifier);
    boolean hasModifier(UUID modifierId);
    void addTemporaryModifier(AttributeModifier<T> modifier);
    void removeModifier(AttributeModifier<T> modifier);
    void removeModifier(UUID modifierId);
    T getValue();
    T getBaseValue();
    void write(PacketByteBuf buf);
    List<AttributeModifier<T>> getModifiers();
    default boolean isDirty()
    {
        return false;
    }
}
