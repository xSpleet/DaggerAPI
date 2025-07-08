package xspleet.daggerapi.attributes.instance;

import net.minecraft.network.PacketByteBuf;
import xspleet.daggerapi.attributes.modifier.AttributeModifier;

import java.util.UUID;

public interface AttributeInstance<T>
{
    public String getAttributeName();
    public boolean hasModifier(AttributeModifier<T> modifier);
    public boolean hasModifier(UUID modifierId);
    public void addTemporaryModifier(AttributeModifier<T> modifier);
    public void removeModifier(AttributeModifier<T> modifier);
    public T getValue();
    public T getBaseValue();
    public void clean();
    public boolean isDirty();
    public void write(PacketByteBuf buf);
}
