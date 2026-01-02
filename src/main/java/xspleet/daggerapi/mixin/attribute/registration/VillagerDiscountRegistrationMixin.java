package xspleet.daggerapi.mixin.attribute.registration;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xspleet.daggerapi.api.collections.Attributes;
import xspleet.daggerapi.attributes.AttributeHolder;

@Mixin(VillagerEntity.class)
public class VillagerDiscountRegistrationMixin
{
    @Inject(method = "prepareOffersFor", at = @At("TAIL"))
    private void addDiscountToTrades(PlayerEntity player, CallbackInfo ci)
    {
        double discount = AttributeHolder.asHolder(player).getAttributeInstance(Attributes.VILLAGER_DISCOUNT).getValue();
        if(discount == 0)
            return;
        for (TradeOffer tradeOffer : ((VillagerEntity) (Object) this).getOffers())
        {
            tradeOffer.increaseSpecialPrice((int) -Math.round(tradeOffer.getAdjustedFirstBuyItem().getCount()*discount));
        }
    }
}
