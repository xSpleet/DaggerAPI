package xspleet.daggerapi.attributes.mixin;

import net.minecraft.network.PacketByteBuf;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.attributes.container.SyncContainer;
import xspleet.daggerapi.attributes.instance.AttributeInstance;

public interface MixinAttributeHolder extends AttributeHolder
{
    public <T> AttributeInstance<T> DaggerAPI$getAttributeInstance(Attribute<T> attribute);
    public void DaggerAPI$syncAttributeContainer();
    public void DaggerAPI$acceptSyncContainer(SyncContainer syncContainer);
    public boolean DaggerAPI$needsAttributeSync();

    public default <T> AttributeInstance<T> getAttributeInstance(Attribute<T> attribute) {
        return DaggerAPI$getAttributeInstance(attribute);
    }

    public default void syncAttributeContainer() {
        DaggerAPI$syncAttributeContainer();
    }

    public default void acceptSyncContainer(SyncContainer syncContainer) {
        DaggerAPI$acceptSyncContainer(syncContainer);
    }

    public default boolean needsAttributeSync() {
        return DaggerAPI$needsAttributeSync();
    }
}


