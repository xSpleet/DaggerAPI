package xspleet.daggerapi.base.artifact;

import java.util.*;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xspleet.daggerapi.base.*;
import xspleet.daggerapi.base.actions.ConditionalAction;
import xspleet.daggerapi.base.actions.WeightedConditionalAction;


public class ArtifactItem extends TrinketItem
{
    protected final List<ArtifactAttributeModifier> attributeModifiers;
    protected ArtifactRarity artifactRarity = ArtifactRarity.COMMON;

    protected Map<Trigger, List<ConditionalAction>> events;
    protected Map<Trigger, List<WeightedConditionalAction>> weightedEvents;

    public ArtifactItem(Settings settings) {
        super(settings);
        attributeModifiers = new ArrayList<>();
        registerAttributeModifiers();
        events = new HashMap<>();
        weightedEvents = new HashMap<>();
    }

    public void receiveTrigger(Trigger trigger, DaggerData data)
    {
        events.getOrDefault(trigger, new ArrayList<>()).forEach(a -> a.actOn(data));

        List<WeightedConditionalAction> actions = weightedEvents.get(trigger);
        if(actions.isEmpty())
            return;

        int totalWeight = actions
                .stream()
                .mapToInt(WeightedConditionalAction::getWeight)
                .reduce(0, Integer::sum);

        int currentWeight = 0;
        int random = (new Random()).nextInt(totalWeight);

        for(WeightedConditionalAction action: actions)
        {
            currentWeight += action.getWeight();
            if(currentWeight > random)
            {
                action.actOn(data);
                return;
            }
        }
    }

    public void registerAttributeModifiers(){}

    public void updatePlayer(PlayerEntity player)
    {
        for(ArtifactAttributeModifier attributeModifier: attributeModifiers)
            attributeModifier.updatePlayer(player);
    }

    public void cleansePlayer(PlayerEntity player)
    {
        for(ArtifactAttributeModifier attributeModifier: attributeModifiers)
            attributeModifier.cleansePlayer(player);
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.onEquip(stack, slot, entity);
        if(entity instanceof PlayerEntity player)
            updatePlayer(player);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);
        if(entity instanceof PlayerEntity player)
            updatePlayer(player);
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.onUnequip(stack, slot, entity);
        if(entity instanceof PlayerEntity player)
            cleansePlayer(player);
    }

    public ArtifactItem rarity(ArtifactRarity artifactRarity)
    {
        this.artifactRarity = artifactRarity;
        return this;
    }
    private void addArtifactRarity(List<Text> tooltip)
    {
        switch (artifactRarity)
        {
            case COMMON: {
                tooltip.add(Text.translatable("item.magpie.rarity.common").formatted(Formatting.BOLD).formatted(Formatting.GRAY));
                break;
            }
            case RARE: {
                tooltip.add(Text.translatable("item.magpie.rarity.rare").formatted(Formatting.BOLD).formatted(Formatting.BLUE));
                break;
            }
            case EPIC: {
                tooltip.add(Text.translatable("item.magpie.rarity.epic").formatted(Formatting.BOLD).formatted(Formatting.DARK_PURPLE));
                break;
            }
            case LEGENDARY: {
                tooltip.add(Text.translatable("item.magpie.rarity.legendary").formatted(Formatting.BOLD).formatted(Formatting.GOLD));
                break;
            }
        }
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) 
    {
        if(TrinketsUtil.hasArtifact(entity, stack.getItem()))
        {
            return false;
        }
        return super.canEquip(stack, slot, entity);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        addArtifactRarity(tooltip);
        if(Screen.hasShiftDown())
        {
            if(stack.getItem() instanceof ActiveArtifactItem activeArtifact)
            {
                tooltip.add(Text.translatable("item.magpie.tooltip.recharge", activeArtifact.getCooldown()/20 + " "));
            }
            TextFormatter.addTooltips(Text.translatable("item.magpie.tooltip." + stack.getItem()),tooltip);
        }
        else
        {
            if(stack.getItem() instanceof ActiveArtifactItem)
                TextFormatter.addTooltips(Text.translatable("item.magpie.tooltip.active"), tooltip);
            TextFormatter.addTooltips(Text.translatable("item.magpie.description." + stack.getItem()), tooltip);
            tooltip.add(Text.translatable("item.magpie.tooltip.shiftmoreinfo"));
        }
    }
}
