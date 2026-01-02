package xspleet.daggerapi.mixin.attribute.registration;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.attributes.container.DaggerAttributeContainer;
import xspleet.daggerapi.attributes.container.SyncContainer;
import xspleet.daggerapi.attributes.instance.AttributeInstance;
import xspleet.daggerapi.attributes.mixin.MixinAttribute;
import xspleet.daggerapi.attributes.mixin.MixinAttributeHolder;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.util.Self;
import xspleet.daggerapi.api.collections.Attributes;
import xspleet.daggerapi.networking.NetworkingConstants;

import static xspleet.daggerapi.api.logging.LoggingContext.SYNC;

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

    @Override
    public void DaggerAPI$syncAttributeContainer() {
        if(self() instanceof ServerPlayerEntity player)
        {
            DaggerLogger.debug(SYNC,"Syncing attributes for player {}: {}", player.getName().getString(), daggerAPI$attributeContainer.getSyncLogMessage());
            var packet = daggerAPI$attributeContainer.toPacketByteBuf();
            ServerPlayNetworking.send(player, NetworkingConstants.SYNC_ATTRIBUTES_PACKET_ID, packet);
        }
    }

    @Override
    public void DaggerAPI$acceptSyncContainer(SyncContainer syncContainer) {
        if(self().getWorld().isClient()) {
            if (daggerAPI$attributeContainer == null) {
                throw new IllegalStateException("Attribute container is not initialized.");
            }
            DaggerLogger.debug(SYNC,"Accepting sync container for player {}: {}", self().getName().getString() , syncContainer.getSyncLogMessage());
            daggerAPI$attributeContainer.acceptSyncContainer(syncContainer);
        }
    }

    @Override
    public boolean DaggerAPI$needsAttributeSync() {
        return daggerAPI$attributeContainer.needsSync();
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void daggerAPI$init(EntityType<? extends LivingEntity> entityType, World world, CallbackInfo ci) {
        var container = new DaggerAttributeContainer.Builder()
                .addAttribute(Attributes.JUMP_HEIGHT)
                .addAttribute(Attributes.CAN_WALK_ON_WATER)
                .addAttribute(Attributes.STATIC_AQUA_AFFINITY)
                .addAttribute(Attributes.STATIC_EFFICIENCY)
                .addAttribute(Attributes.STATIC_DEPTH_STRIDER)
                .addAttribute(Attributes.STATIC_LOOTING)
                .addAttribute(Attributes.STATIC_RESPIRATION)
                .addAttribute(Attributes.STATIC_FIRE_ASPECT)
                .addAttribute(Attributes.STATIC_SWIFT_SNEAK)
                .addAttribute(Attributes.STATIC_KNOCKBACK)
                .addAttribute(Attributes.VILLAGER_DISCOUNT)
                .addAttribute(Attributes.EXTRA_JUMPS);
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

    @ModifyVariable(
            method = "computeFallDamage",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 2
    )
    private float daggerAPI$modifyFallDamage(float f) {
        if (self() instanceof PlayerEntity player && player instanceof AttributeHolder holder) {
            var jumpHeight = holder.getAttributeInstance(Attributes.JUMP_HEIGHT);
            if (jumpHeight != null) {
                return f + (int) Math.round((jumpHeight.getValue()-jumpHeight.getBaseValue())/jumpHeight.getBaseValue());
            }
        }
        return f;
    }

    @ModifyExpressionValue(
            method = "canWalkOnFluid",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=0")
    )
    private int daggerAPI$canWalkOnFluid(int original, @Local(argsOnly = true) FluidState state) {
        if(self() instanceof PlayerEntity player && player instanceof AttributeHolder holder) {
            var canWalkOnWater = holder.getAttributeInstance(Attributes.CAN_WALK_ON_WATER);
            if(state.isOf(Fluids.WATER) && canWalkOnWater != null && canWalkOnWater.getValue()) {
                return (canWalkOnWater.getValue() && self().getFluidHeight(FluidTags.WATER) < 0.39 && self().getFluidHeight(FluidTags.WATER) > 0) ? 1 : 0;
            }
        }
        return original;
    }
}
