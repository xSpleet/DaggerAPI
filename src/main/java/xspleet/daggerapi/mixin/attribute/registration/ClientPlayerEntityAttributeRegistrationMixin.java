package xspleet.daggerapi.mixin.attribute.registration;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xspleet.daggerapi.api.collections.Attributes;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.util.TrinketsUtil;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityAttributeRegistrationMixin
{
    @Unique
    int jumps = 0;

    @Unique
    boolean jumped = false;

    @Unique
    private int extraJumps(ClientPlayerEntity player)
    {
        return AttributeHolder.asHolder(player).getAttributeInstance(Attributes.EXTRA_JUMPS).getValue();
    }

    @Inject(method = "tickMovement", at = @At(value = "HEAD"))
    private void addExtraJumps(CallbackInfo ci)
    {
        ClientPlayerEntity player = (ClientPlayerEntity)(Object)this;
        if(player.isClimbing() || player.isOnGround())
        {
            jumps = extraJumps(player);
        }
        else if(!jumped && jumps > 0 && player.getVelocity().y < 0)
        {
            if(player.input.jumping && !player.getAbilities().flying)
            {
                ItemStack stack = player.getEquippedStack(EquipmentSlot.CHEST);
                boolean b1 = stack.getItem() == Items.ELYTRA && ElytraItem.isUsable(stack);
                boolean canJump = !b1 && !player.isFallFlying() && !player.hasVehicle() && !player.isTouchingWater() && !player.hasStatusEffect(StatusEffects.LEVITATION);
                if(canJump)
                {
                    jumps--;
                    player.jump();
                }
            }
        }
        jumped = player.input.jumping;
    }
}
