package xspleet.daggerapi.attributes.container;

import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.instance.AttributeInstance;

import java.util.HashMap;

public class SyncAttributeContainer extends DaggerAttributeContainer
{
    public SyncAttributeContainer(HashMap<Attribute, AttributeInstance> attributeInstances)
    {
        super(attributeInstances);
    }
}
