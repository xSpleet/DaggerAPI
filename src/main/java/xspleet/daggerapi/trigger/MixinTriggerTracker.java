package xspleet.daggerapi.trigger;

import net.minecraft.item.Item;

import java.util.List;

public interface MixinTriggerTracker extends TriggerTracker
{
    void daggerAPI$setTrackingEnabled(boolean enabled);
    boolean daggerAPI$isTrackingEnabled();
    void daggerAPI$trigger(Trigger trigger, Item item, long tick);
    List<TriggerTrackEntry> daggerAPI$getTriggerTrackEntries(long tick);

    default List<TriggerTrackEntry> getTriggerTrackEntries(long tick) {
        return daggerAPI$getTriggerTrackEntries(tick);
    }

    default void trigger(Trigger trigger, Item item, long tick) {
        daggerAPI$trigger(trigger, item, tick);
    }

    default void setTrackingEnabled(boolean enabled) {
        daggerAPI$setTrackingEnabled(enabled);
    }

    default boolean isTrackingEnabled() {
        return daggerAPI$isTrackingEnabled();
    }
}
