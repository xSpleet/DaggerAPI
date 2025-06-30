package xspleet.daggerapi.trigger.registration;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xspleet.daggerapi.collections.Triggers;
import xspleet.daggerapi.data.TriggerData;

@Mixin(LivingEntity.class)
public class LivingEntityTriggersRegistration {
	@Inject(at = @At("HEAD"), method = "damage")
	private void beforeHitTrigger(CallbackInfoReturnable<Boolean> cir) {
		LivingEntity entity = ((LivingEntity)(Object)this);
		Triggers.BEFORE_HIT.trigger(new TriggerData()
				.setTriggerer(entity)
				.setTriggeredWorld(entity.getWorld()));
	}

	@Inject(at = @At("RETURN"), method = "damage")
	private void afterHitTrigger(CallbackInfoReturnable<Boolean> cir)
	{
		if(cir.getReturnValue())
		{
			LivingEntity entity = ((LivingEntity)(Object)this);
			Triggers.AFTER_HIT.trigger(new TriggerData()
					.setTriggerer(entity)
					.setTriggeredWorld(entity.getWorld()));
		}
	}
}