package xspleet.daggerapi.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xspleet.daggerapi.base.Self;
import xspleet.daggerapi.trigger.MixinTriggerTracker;
import xspleet.daggerapi.trigger.Trigger;
import xspleet.daggerapi.trigger.TriggerTrackEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityTriggerTrackerMixin implements Self<ClientPlayerEntity>, MixinTriggerTracker {

    @Unique
    List<TriggerTrackEntry> daggerAPI$triggerTrackEntries = new ArrayList<>();
    @Unique
    private boolean daggerAPI$trackingEnabled = false;

    @Override
    public void daggerAPI$setTrackingEnabled(boolean enabled) {
        if(!enabled)
            daggerAPI$triggerTrackEntries.clear();
        daggerAPI$trackingEnabled = enabled;
    }

    @Override
    public boolean daggerAPI$isTrackingEnabled() {
        return daggerAPI$trackingEnabled;
    }

    @Override
    public void daggerAPI$trigger(Trigger trigger, Item item, long tick) {
        if(!daggerAPI$trackingEnabled) {
            return;
        }
        daggerAPI$triggerTrackEntries.add(new TriggerTrackEntry(trigger, item, tick));
    }

    @Override
    public List<TriggerTrackEntry> daggerAPI$getTriggerTrackEntries(long tick) {
        if(!daggerAPI$trackingEnabled) {
            return List.of();
        }
        daggerAPI$triggerTrackEntries.removeIf(entry -> entry.tick() + 100 > tick);
        return daggerAPI$triggerTrackEntries;
    }
}
