package xspleet.daggerapi.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.base.Self;
import xspleet.daggerapi.collections.Triggers;
import xspleet.daggerapi.data.TriggerData;
import xspleet.daggerapi.data.key.DaggerKeys;
import xspleet.daggerapi.trigger.TriggerTracker;

@Mixin(LivingEntity.class)
public class LivingEntityTriggersRegistration implements Self<LivingEntity> {

	@WrapMethod(method = "damage")
	private boolean beforeDamage(DamageSource source, float amount, Operation<Boolean> original)
	{
		LivingEntity entity = self();

		var data = new TriggerData()
				.addData(DaggerKeys.TRIGGERER, entity)
				.addData(DaggerKeys.WORLD, entity.getWorld())
				.addData(DaggerKeys.AMOUNT, (double)amount)
				.addData(DaggerKeys.DAMAGE_SOURCE, source);

		Triggers.BEFORE_DAMAGE.trigger(data);

		source = data.getData(DaggerKeys.DAMAGE_SOURCE);
		amount = data.getData(DaggerKeys.AMOUNT).floatValue();

		return original.call(source, amount);
	}
}