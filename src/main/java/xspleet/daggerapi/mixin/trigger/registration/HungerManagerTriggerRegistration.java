package xspleet.daggerapi.mixin.trigger.registration;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xspleet.daggerapi.api.collections.DaggerKeys;
import xspleet.daggerapi.api.collections.Triggers;
import xspleet.daggerapi.data.collection.TriggerData;
import xspleet.daggerapi.util.PlayerOwned;
import xspleet.daggerapi.util.Self;

@Mixin(HungerManager.class)
public class HungerManagerTriggerRegistration implements Self<HungerManager>
{
    @WrapOperation(
            method = "eat",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/HungerManager;add(IF)V"
            )
    )
    private void daggerapi$registerEatTrigger(
            HungerManager instance,
            int food,
            float saturationModifier,
            Operation<Void> original,
            @Local(argsOnly = true) Item item,
            @Local(argsOnly = true) ItemStack stack)
    {
        if(!(self() instanceof PlayerOwned playerOwned)) {
            original.call(instance, food, saturationModifier);
            return;
        }

        var data = new TriggerData()
                .addData(DaggerKeys.TRIGGER_SOURCE, playerOwned.daggerapi$getOwner())
                .addData(DaggerKeys.WORLD, playerOwned.daggerapi$getOwner().getWorld())
                .addData(DaggerKeys.FOOD_AMOUNT, food)
                .addData(DaggerKeys.SATURATION_AMOUNT, saturationModifier)
                .addData(DaggerKeys.ITEM_STACK, stack)
                .addData(DaggerKeys.ITEM, item);

        Triggers.EAT.trigger(data);

        self().add(data.getData(DaggerKeys.FOOD_AMOUNT), data.getData(DaggerKeys.SATURATION_AMOUNT));
    }
}
