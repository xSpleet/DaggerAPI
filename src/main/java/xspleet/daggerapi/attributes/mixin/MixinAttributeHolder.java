package xspleet.daggerapi.attributes.mixin;

import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.attributes.container.SyncAttributeContainer;
import xspleet.daggerapi.attributes.instance.AttributeInstance;

public interface MixinAttributeHolder extends AttributeHolder
{
    public <T> AttributeInstance<T> DaggerAPI$getAttributeInstance(Attribute<T> attribute);
    public void DaggerAPI$syncAttributeContainer();
    public void DaggerAPI$acceptSyncContainer(SyncAttributeContainer syncContainer);

    public default <T> AttributeInstance<T> getAttributeInstance(Attribute<T> attribute) {
        return DaggerAPI$getAttributeInstance(attribute);
    }

    public default void syncAttributeContainer() {
        DaggerAPI$syncAttributeContainer();
    }

    public default void acceptSyncContainer(SyncAttributeContainer syncContainer) {
        DaggerAPI$acceptSyncContainer(syncContainer);
    }
}


