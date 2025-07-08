package xspleet.daggerapi.attributes;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import xspleet.daggerapi.attributes.instance.AttributeInstance;

public interface AttributeHolder
{
    public <T> AttributeInstance<T> getAttributeInstance(Attribute<T> attribute);
    public static AttributeHolder asHolder(LivingEntity livingEntity)
    {
        if(livingEntity instanceof AttributeHolder holder)
            return holder;
        throw new IllegalArgumentException("LivingEntity does not implement AttributeHolder");
    }
}
