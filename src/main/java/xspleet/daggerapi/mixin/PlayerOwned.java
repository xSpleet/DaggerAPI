package xspleet.daggerapi.mixin;

import net.minecraft.entity.player.PlayerEntity;

public interface PlayerOwned
{
    public PlayerEntity daggerapi$getOwner();
    public void daggerapi$setOwner(PlayerEntity player);
}
