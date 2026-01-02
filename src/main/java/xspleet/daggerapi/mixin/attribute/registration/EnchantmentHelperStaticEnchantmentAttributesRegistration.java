package xspleet.daggerapi.mixin.attribute.registration;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import xspleet.daggerapi.api.collections.Attributes;
import xspleet.daggerapi.attributes.AttributeHolder;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperStaticEnchantmentAttributesRegistration
{
    @ModifyExpressionValue(
            method = "getSwiftSneakSpeedBoost",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getEquipmentLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/entity/LivingEntity;)I")
    )
    private static int addStaticSwiftSneakSpeed(int original, LivingEntity entity)
    {
        return original + AttributeHolder.asHolder(entity).getAttributeInstance(Attributes.STATIC_SWIFT_SNEAK).getValue();
    }

    @ModifyReturnValue(
            method = "getEfficiency",
            at = @At("RETURN")
    )
    private static int addStaticEfficiency(int original, LivingEntity entity)
    {
        return original + AttributeHolder.asHolder(entity).getAttributeInstance(Attributes.STATIC_EFFICIENCY).getValue();
    }

    @ModifyReturnValue(
            method = "getKnockback",
            at = @At("RETURN")
    )
    private static int addStaticKnockback(int original, LivingEntity entity)
    {
        return original + AttributeHolder.asHolder(entity).getAttributeInstance(Attributes.STATIC_KNOCKBACK).getValue();
    }

    @ModifyReturnValue(
            method = "getDepthStrider",
            at = @At("RETURN")
    )
    private static int addStaticDepthStrider(int original, LivingEntity entity)
    {
        return original + AttributeHolder.asHolder(entity).getAttributeInstance(Attributes.STATIC_DEPTH_STRIDER).getValue();
    }

    @ModifyReturnValue(
            method = "getFireAspect",
            at = @At("RETURN")
    )
    private static int addStaticFireAspect(int original, LivingEntity entity)
    {
        return original + AttributeHolder.asHolder(entity).getAttributeInstance(Attributes.STATIC_FIRE_ASPECT).getValue();
    }

    @ModifyReturnValue(
            method = "getRespiration",
            at = @At("RETURN")
    )
    private static int addStaticRespiration(int original, LivingEntity entity)
    {
        return original + AttributeHolder.asHolder(entity).getAttributeInstance(Attributes.STATIC_RESPIRATION).getValue();
    }

    @ModifyReturnValue(
            method = "getLooting",
            at = @At("RETURN")
    )
    private static int addStaticLooting(int original, LivingEntity entity)
    {
        return original + AttributeHolder.asHolder(entity).getAttributeInstance(Attributes.STATIC_LOOTING).getValue();
    }

    @ModifyExpressionValue(
            method = "hasAquaAffinity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getEquipmentLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/entity/LivingEntity;)I")
    )
    private static int addStaticAquaAffinity(int original, LivingEntity entity)
    {
        return original + AttributeHolder.asHolder(entity).getAttributeInstance(Attributes.STATIC_AQUA_AFFINITY).getValue();
    }
}
