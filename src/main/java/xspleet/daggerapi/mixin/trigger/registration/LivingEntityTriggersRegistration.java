package xspleet.daggerapi.mixin.trigger.registration;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import xspleet.daggerapi.util.Self;
import xspleet.daggerapi.api.collections.Triggers;
import xspleet.daggerapi.data.collection.TriggerData;
import xspleet.daggerapi.api.collections.DaggerKeys;

@Mixin(LivingEntity.class)
public class LivingEntityTriggersRegistration implements Self<LivingEntity> {

	@WrapMethod(method = "damage")
	private boolean beforeDamage(DamageSource source, float amount, Operation<Boolean> original)
	{
		LivingEntity entity = self();

		var data = new TriggerData()
				.addData(DaggerKeys.TRIGGER_SOURCE, entity)
				.addData(DaggerKeys.WORLD, entity.getWorld())
				.addData(DaggerKeys.DAMAGE_AMOUNT, (double)amount)
				.addData(DaggerKeys.DAMAGE_SOURCE, source);

		Triggers.BEFORE_DAMAGE.trigger(data);

		source = data.getData(DaggerKeys.DAMAGE_SOURCE);
		amount = data.getData(DaggerKeys.DAMAGE_AMOUNT).floatValue();

		if (amount <= 0) {
			return false; // Prevents damage if amount is zero or negative
		}

		return original.call(source, amount);
	}
}