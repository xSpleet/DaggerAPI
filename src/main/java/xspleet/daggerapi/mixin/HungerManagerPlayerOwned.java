package xspleet.daggerapi.mixin;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xspleet.daggerapi.util.PlayerOwned;

@Mixin(HungerManager.class)
public class HungerManagerPlayerOwned implements PlayerOwned
{
    @Unique
    private PlayerEntity owner;

    @Override
    public PlayerEntity daggerapi$getOwner() {
        return owner;
    }

    @Override
    public void daggerapi$setOwner(PlayerEntity player) {
        this.owner = player;
    }
}
