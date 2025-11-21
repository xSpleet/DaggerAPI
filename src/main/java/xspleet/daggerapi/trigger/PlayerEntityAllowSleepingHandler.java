package xspleet.daggerapi.trigger;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class PlayerEntityAllowSleepingHandler implements EntitySleepEvents.AllowNearbyMonsters {

    @Override
    public ActionResult allowNearbyMonsters(PlayerEntity player, BlockPos sleepingPos, boolean vanillaResult) {
        return ActionResult.PASS;
    }
}
