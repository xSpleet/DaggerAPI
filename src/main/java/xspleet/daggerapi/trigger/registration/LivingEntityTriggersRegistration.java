package xspleet.daggerapi.trigger.registration;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xspleet.daggerapi.collections.Triggers;
import xspleet.daggerapi.data.TriggerData;
import xspleet.daggerapi.data.key.DaggerKeys;

@Mixin(LivingEntity.class)
public class LivingEntityTriggersRegistration {
	@Inject(at = @At("HEAD"), method = "damage")
	private void beforeHitTrigger(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

	}

	@WrapMethod(method = "damage")
	private boolean beforeDamage(DamageSource source, float amount, Operation<Boolean> original)
	{
		LivingEntity entity = ((LivingEntity)(Object)this);

		if(entity.getWorld().isClient)
			return original.call(source, amount); // Don't trigger on the client side

		var data = new TriggerData()
				.addData(DaggerKeys.TRIGGERER, entity)
				.addData(DaggerKeys.WORLD, entity.getWorld())
				.addData(DaggerKeys.AMOUNT, amount)
				.addData(DaggerKeys.DAMAGE_SOURCE, source);

		Triggers.BEFORE_DAMAGE.trigger(data);

		source = data.getData(DaggerKeys.DAMAGE_SOURCE);
		amount = data.getData(DaggerKeys.AMOUNT);

		return original.call(source, amount);
	}
}