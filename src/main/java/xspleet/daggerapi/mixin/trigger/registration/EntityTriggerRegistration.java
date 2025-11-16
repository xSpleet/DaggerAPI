package xspleet.daggerapi.mixin.trigger.registration;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xspleet.daggerapi.api.collections.DaggerKeys;
import xspleet.daggerapi.api.collections.Triggers;
import xspleet.daggerapi.data.collection.TriggerData;
import xspleet.daggerapi.util.Self;

@Mixin(Entity.class)
public class EntityTriggerRegistration implements Self<Entity>
{
    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void daggerapi$registerTickTrigger(CallbackInfo ci)
    {
        Triggers.TICK.trigger(
                new TriggerData()
                        .addData(DaggerKeys.TRIGGER_SOURCE, self())
                        .addData(DaggerKeys.WORLD, self().getWorld())
        );
    }
}
