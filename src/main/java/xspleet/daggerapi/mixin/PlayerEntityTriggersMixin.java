package xspleet.daggerapi.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xspleet.jdagapi.collections.Triggers;

@Mixin(PlayerEntity.class)
public class PlayerEntityTriggersMixin {
	@Inject(at = @At("HEAD"), method = "damage")
	private void beforeHitTrigger(CallbackInfoReturnable<Boolean> cir) {
		PlayerEntity player = ((PlayerEntity)(Object)this);
		Triggers.BEFORE_HIT.trigger(player);
	}

	@Inject(at = @At("RETURN"), method = "damage")
	private void afterHitTrigger(CallbackInfoReturnable<Boolean> cir)
	{
		if(cir.getReturnValue())
		{
			PlayerEntity player = ((PlayerEntity)(Object)this);
			Triggers.AFTER_HIT.trigger(player);
		}
	}
}