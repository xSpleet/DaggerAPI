package xspleet.daggerapi.mixin.trigger.registration;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xspleet.daggerapi.api.collections.DaggerKeys;
import xspleet.daggerapi.api.collections.Triggers;
import xspleet.daggerapi.data.collection.TriggerData;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.util.Self;

@Mixin(EnderPearlEntity.class)
public class EnderPearlEntityTeleportTriggerRegistration implements Self<EnderPearlEntity>
{
    @Unique
    boolean daggerapi$shouldTeleport = true;

    @Unique
    boolean daggerapi$shouldTakeFallDamage = true;

    @Inject(
            method = "onCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;isConnectionOpen()Z",
                    shift = At.Shift.AFTER
            )
    )
    private void daggerapi$registerTeleportTrigger(HitResult hitResult, CallbackInfo ci)
    {
        var data = new TriggerData()
                .addData(DaggerKeys.TRIGGER_SOURCE, self().getOwner())
                .addData(DaggerKeys.WORLD, self().getWorld())
                .addData(DaggerKeys.X, self().getX())
                .addData(DaggerKeys.Y, self().getY())
                .addData(DaggerKeys.Z, self().getZ())
                .addData(DaggerKeys.ALLOW_TELEPORT, true)
                .addData(DaggerKeys.ALLOW_FALL_DAMAGE, true);

        Triggers.BEFORE_ENDER_PEARL_TELEPORT.trigger(data);

        daggerapi$shouldTeleport = data.getData(DaggerKeys.ALLOW_TELEPORT);
        daggerapi$shouldTakeFallDamage = data.getData(DaggerKeys.ALLOW_FALL_DAMAGE);
    }

    @WrapOperation(
            method = "onCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;requestTeleportAndDismount(DDD)V"
            )
    )
    private void daggerapi$wrapTeleportAndDismount(
            ServerPlayerEntity instance, double destX, double destY, double destZ, Operation<Void> original
    ) {
        if (daggerapi$shouldTeleport) {
            original.call(instance, destX, destY, destZ);
            Triggers.ENDER_PEARL_TELEPORT.trigger(
                    new TriggerData()
                            .addData(DaggerKeys.TRIGGER_SOURCE, self().getOwner())
                            .addData(DaggerKeys.WORLD, self().getWorld())
            );
        }
    }

    @WrapOperation(
            method = "onCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;requestTeleport(DDD)V"
            )
    )
    private void daggerapi$wrapTeleport(
            Entity instance, double destX, double destY, double destZ, Operation<Void> original
    ) {
        if (daggerapi$shouldTeleport) {
            original.call(instance, destX, destY, destZ);
            Triggers.ENDER_PEARL_TELEPORT.trigger(
                    new TriggerData()
                            .addData(DaggerKeys.TRIGGER_SOURCE, self().getOwner())
                            .addData(DaggerKeys.WORLD, self().getWorld())
            );
        }
    }

    @WrapOperation(
            method = "onCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            )
    )
    private boolean daggerapi$wrapDamage(
            Entity instance, net.minecraft.entity.damage.DamageSource source, float amount, Operation<Boolean> original
    ) {
        if (daggerapi$shouldTakeFallDamage) {
            return original.call(instance, source, amount);
        }
        return false;
    }
}
