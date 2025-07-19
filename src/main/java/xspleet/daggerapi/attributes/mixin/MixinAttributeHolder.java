package xspleet.daggerapi.attributes.mixin;

import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.attributes.container.SyncContainer;
import xspleet.daggerapi.attributes.instance.AttributeInstance;

public interface MixinAttributeHolder extends AttributeHolder
{
    <T> AttributeInstance<T> DaggerAPI$getAttributeInstance(Attribute<T> attribute);
    void DaggerAPI$syncAttributeContainer();
    void DaggerAPI$acceptSyncContainer(SyncContainer syncContainer);
    boolean DaggerAPI$needsAttributeSync();

    default <T> AttributeInstance<T> getAttributeInstance(Attribute<T> attribute) {
        return DaggerAPI$getAttributeInstance(attribute);
    }

    default void syncAttributeContainer() {
        DaggerAPI$syncAttributeContainer();
    }

    default void acceptSyncContainer(SyncContainer syncContainer) {
        DaggerAPI$acceptSyncContainer(syncContainer);
    }

    default boolean needsAttributeSync() {
        return DaggerAPI$needsAttributeSync();
    }
}


