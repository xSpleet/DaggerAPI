package xspleet.daggerapi.trigger;

import net.minecraft.item.Item;

import java.util.List;

public interface TriggerTracker
{
    void setTrackingEnabled(boolean enabled);
    boolean isTrackingEnabled();
    void trigger(Trigger trigger, Item item, long tick);
    List<TriggerTrackEntry> getTriggerTrackEntries(long tick);
}
