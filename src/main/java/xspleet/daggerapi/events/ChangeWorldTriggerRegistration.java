package xspleet.daggerapi.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import xspleet.daggerapi.api.collections.DaggerKeys;
import xspleet.daggerapi.api.collections.Triggers;
import xspleet.daggerapi.data.collection.TriggerData;

public class ChangeWorldTriggerRegistration implements ServerEntityWorldChangeEvents.AfterPlayerChange {
    @Override
    public void afterChangeWorld(ServerPlayerEntity player, ServerWorld origin, ServerWorld destination) {
        var data = new TriggerData()
                .addData(DaggerKeys.WORLD, destination)
                .addData(DaggerKeys.ORIGIN, origin)
                .addData(DaggerKeys.DESTINATION, destination)
                .addData(DaggerKeys.TRIGGER_SOURCE, player);

        Triggers.ON_CHANGE_DIMENSION.trigger(data);
    }
}
