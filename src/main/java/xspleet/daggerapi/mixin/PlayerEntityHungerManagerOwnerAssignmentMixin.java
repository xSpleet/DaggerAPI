package xspleet.daggerapi.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xspleet.daggerapi.util.PlayerOwned;
import xspleet.daggerapi.util.Self;

@Mixin(PlayerEntity.class)
public class PlayerEntityHungerManagerOwnerAssignmentMixin implements Self<PlayerEntity>
{

    @Shadow
    protected HungerManager hungerManager;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void assignHungerManagerOwner(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci)
    {
        if(hungerManager instanceof PlayerOwned playerOwnerHungerManager)
        {
            playerOwnerHungerManager.daggerapi$setOwner(self());
        }
    }
}
