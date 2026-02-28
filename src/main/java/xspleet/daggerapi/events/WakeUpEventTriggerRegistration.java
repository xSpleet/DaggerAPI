package xspleet.daggerapi.events;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import xspleet.daggerapi.api.collections.DaggerKeys;
import xspleet.daggerapi.api.collections.Triggers;
import xspleet.daggerapi.data.collection.TriggerData;

public class WakeUpEventTriggerRegistration implements EntitySleepEvents.StopSleeping {
    @Override
    public void onStopSleeping(LivingEntity livingEntity, BlockPos blockPos) {
        var wakeUpData = new TriggerData()
                .addData(DaggerKeys.TRIGGER_SOURCE, livingEntity)
                .addData(DaggerKeys.WORLD, livingEntity.getWorld());

        Triggers.ON_WAKE_UP.trigger(wakeUpData);
    }
}
