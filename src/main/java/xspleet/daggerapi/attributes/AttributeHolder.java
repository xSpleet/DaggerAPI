package xspleet.daggerapi.attributes;

import net.minecraft.entity.player.PlayerEntity;
import xspleet.daggerapi.attributes.instance.AttributeInstance;

public interface AttributeHolder
{
    public <T> AttributeInstance<T> getAttributeInstance(Attribute<T> attribute);
    public static AttributeHolder asHolder(PlayerEntity player)
    {
        if(player instanceof AttributeHolder holder)
            return holder;
        throw new IllegalArgumentException("PlayerEntity does not implement AttributeHolder");
    }
}
