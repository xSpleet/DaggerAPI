package xspleet.daggerapi.attributes.container;

import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.instance.AttributeInstance;

import java.util.HashMap;
import java.util.Map;

public class SyncAttributeContainer extends DaggerAttributeContainer
{
    protected SyncAttributeContainer(HashMap<Attribute<?>, AttributeInstance<?>> attributeInstances)
    {
        super(attributeInstances);
    }

    protected Map<Attribute<?>, AttributeInstance<?>> getAttributeInstances()
    {
        return attributeInstances;
    }

    @Override
    public void acceptSyncContainer(SyncAttributeContainer syncContainer) {
        throw new IllegalStateException("SyncAttributeContainer cannot accept another SyncAttributeContainer");
    }
}
