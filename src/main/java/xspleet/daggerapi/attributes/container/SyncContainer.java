package xspleet.daggerapi.attributes.container;

import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.instance.AttributeInstanceSyncData;

import java.util.Map;

public class SyncContainer
{
    private Map<Attribute<?>, AttributeInstanceSyncData> syncData;

    public SyncContainer(Map<Attribute<?>, AttributeInstanceSyncData> syncData) {
        this.syncData = syncData;
    }

    public Map<Attribute<?>, AttributeInstanceSyncData> getSyncData() {
        return syncData;
    }
}
