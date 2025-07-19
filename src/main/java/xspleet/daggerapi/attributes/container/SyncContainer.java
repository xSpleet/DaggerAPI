package xspleet.daggerapi.attributes.container;

import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.instance.AttributeInstanceSyncData;

import java.util.Map;

public class SyncContainer
{
    private final Map<Attribute<?>, AttributeInstanceSyncData> syncData;

    public SyncContainer(Map<Attribute<?>, AttributeInstanceSyncData> syncData) {
        this.syncData = syncData;
    }

    public Map<Attribute<?>, AttributeInstanceSyncData> getSyncData() {
        return syncData;
    }

    public String getSyncLogMessage() {
        StringBuilder sb = new StringBuilder("SyncContainer: ");
        syncData.forEach((attribute, data) -> sb.append(attribute.getName()).append("\n"));
        return sb.toString();
    }
}
