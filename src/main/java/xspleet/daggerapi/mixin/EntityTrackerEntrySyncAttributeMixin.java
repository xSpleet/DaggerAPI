package xspleet.daggerapi.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.base.Self;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntrySyncAttributeMixin implements Self<EntityTrackerEntry>
{

    @Final
    @Shadow
    private Entity entity;

    @Inject(
            method = "startTracking",
            at = @At(
                    value = "HEAD"
            )
    )
    private void daggerAPI$syncAttributes(ServerPlayerEntity player, CallbackInfo ci) {
        AttributeHolder.asHolder(player).syncAttributeContainer();
    }

    @Inject(
            method = "syncEntityData",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Set;clear()V",
                    shift = At.Shift.AFTER)
    )
    private void daggerAPI$syncAttributesOnDataClear(CallbackInfo ci) {
        if (entity instanceof AttributeHolder holder) {
            holder.syncAttributeContainer();
        }
    }
}
