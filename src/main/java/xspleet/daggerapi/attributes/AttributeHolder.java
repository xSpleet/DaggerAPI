package xspleet.daggerapi.attributes;

import net.minecraft.entity.LivingEntity;
import xspleet.daggerapi.attributes.container.SyncContainer;
import xspleet.daggerapi.attributes.instance.AttributeInstance;

public interface AttributeHolder
{
    public <T> AttributeInstance<T> getAttributeInstance(Attribute<T> attribute);
    public void syncAttributeContainer();
    public void acceptSyncContainer(SyncContainer syncContainer);
    public boolean needsAttributeSync();

    public static AttributeHolder asHolder(LivingEntity livingEntity)
    {
        if(livingEntity instanceof AttributeHolder holder)
            return holder;
        throw new IllegalArgumentException("LivingEntity does not implement AttributeHolder");
    }
}
