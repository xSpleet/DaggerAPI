package xspleet.daggerapi.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import xspleet.daggerapi.api.collections.DaggerKeys;
import xspleet.daggerapi.api.collections.Triggers;
import xspleet.daggerapi.data.collection.TriggerData;

public class AfterDeathEventTriggerRegistration implements ServerLivingEntityEvents.AfterDeath {
    @Override
    public void afterDeath(LivingEntity livingEntity, DamageSource damageSource) {
        var deathData = new TriggerData()
                .addData(DaggerKeys.TRIGGER_SOURCE, livingEntity)
                .addData(DaggerKeys.WORLD, livingEntity.getWorld())
                .addData(DaggerKeys.DAMAGE_SOURCE, damageSource);

        Triggers.DEATH.trigger(deathData);

        if(damageSource.getAttacker() != null)
        {
            var killData = new TriggerData()
                    .addData(DaggerKeys.TRIGGER_SOURCE, damageSource.getAttacker())
                    .addData(DaggerKeys.WORLD, damageSource.getAttacker().getWorld())
                    .addData(DaggerKeys.DAMAGE_SOURCE, damageSource)
                    .addData(DaggerKeys.VICTIM, livingEntity);
            Triggers.KILL.trigger(killData);
        }
    }
}
