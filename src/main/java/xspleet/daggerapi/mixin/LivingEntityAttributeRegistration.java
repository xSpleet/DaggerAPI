package xspleet.daggerapi.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.attributes.container.DaggerAttributeContainer;
import xspleet.daggerapi.attributes.instance.AttributeInstance;
import xspleet.daggerapi.attributes.mixin.MixinAttribute;
import xspleet.daggerapi.attributes.mixin.MixinAttributeHolder;
import xspleet.daggerapi.base.Self;
import xspleet.daggerapi.collections.Attributes;

@Mixin(LivingEntity.class)
public class LivingEntityAttributeRegistration implements Self<LivingEntity>, MixinAttributeHolder {

    @Unique
    private DaggerAttributeContainer daggerAPI$attributeContainer;

    @Unique
    private DaggerAttributeContainer getAttributeContainer()
    {
        return daggerAPI$attributeContainer;
    }

    @Override
    public <T> AttributeInstance<T> DaggerAPI$getAttributeInstance(Attribute<T> attribute) {
        if(attribute instanceof MixinAttribute && attribute instanceof EntityAttribute vanillaAttribute) {
            return (AttributeInstance<T>) self().getAttributeInstance(vanillaAttribute);
        }
        return getAttributeContainer().getAttributeInstance(attribute);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void daggerAPI$init(EntityType<? extends LivingEntity> entityType, World world, CallbackInfo ci) {
        var container = new DaggerAttributeContainer.Builder()
                .addAttribute(Attributes.JUMP_HEIGHT);
        daggerAPI$attributeContainer = container.build();
    }

    @ModifyExpressionValue(
            method = "getJumpVelocity",
            at = @At(
                    value = "CONSTANT",
                    args = "floatValue=0.42F")
    )
    private float daggerAPI$modifyJumpVelocity(float original) {
        if (self() instanceof PlayerEntity player && player instanceof AttributeHolder holder) {
            var jumpHeight = holder.getAttributeInstance(Attributes.JUMP_HEIGHT);
            if (jumpHeight != null) {
                return jumpHeight.getValue().floatValue();
            }
        }
        return original;
    }

    @ModifyArg(
            method = "computeFallDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;ceil(F)I"
            ),
            index = 0
    )
    private float daggerAPI$modifyFallDamage(float original) {
        if (self() instanceof PlayerEntity player && player instanceof AttributeHolder holder) {
            var fallDamage = holder.getAttributeInstance(Attributes.JUMP_HEIGHT);
            if (fallDamage != null) {
                if(fallDamage.getValue().floatValue() > fallDamage.getBaseValue().floatValue()) {
                    return original * fallDamage.getBaseValue().floatValue() / fallDamage.getValue().floatValue();
                }
                return original * fallDamage.getBaseValue().floatValue() / fallDamage.getValue().floatValue();
            }
        }
        return original;
    }
}
