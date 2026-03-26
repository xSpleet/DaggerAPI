package xspleet.daggerapi.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import xspleet.daggerapi.api.collections.DaggerKeys;
import xspleet.daggerapi.api.collections.Triggers;
import xspleet.daggerapi.data.collection.TriggerData;

public class AllowDeathEventTriggerRegistration implements ServerLivingEntityEvents.AllowDeath
{
    @Override
    public boolean allowDeath(LivingEntity livingEntity, DamageSource damageSource, float amount) {
        var data = new TriggerData()
                .addData(DaggerKeys.TRIGGER_SOURCE, livingEntity)
                .addData(DaggerKeys.WORLD, livingEntity.getWorld())
                .addData(DaggerKeys.ALLOW_DEATH, true)
                .addData(DaggerKeys.DAMAGE_SOURCE, damageSource)
                .addData(DaggerKeys.DAMAGE_AMOUNT, (double)amount);

        Triggers.BEFORE_DEATH.trigger(data);

        if(!data.getData(DaggerKeys.ALLOW_DEATH))
            livingEntity.setHealth(1);

        return data.getData(DaggerKeys.ALLOW_DEATH);
    }
}
