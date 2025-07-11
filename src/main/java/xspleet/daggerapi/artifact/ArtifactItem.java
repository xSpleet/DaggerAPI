package xspleet.daggerapi.artifact;

import java.util.*;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.artifact.builder.ArtifactAttributeModifier;
import xspleet.daggerapi.artifact.builder.ArtifactRarity;
import xspleet.daggerapi.base.*;
import xspleet.daggerapi.data.ConditionData;
import xspleet.daggerapi.data.TriggerData;
import xspleet.daggerapi.data.key.DaggerKeys;
import xspleet.daggerapi.exceptions.MissingDataException;
import xspleet.daggerapi.trigger.actions.ConditionalAction;
import xspleet.daggerapi.trigger.Trigger;
import xspleet.daggerapi.trigger.actions.WeightedConditionalAction;


public class ArtifactItem extends TrinketItem {
    protected final List<ArtifactAttributeModifier> attributeModifiers;
    protected ArtifactRarity artifactRarity = ArtifactRarity.COMMON;

    protected final Map<Trigger, List<ConditionalAction>> events;
    protected final Map<Trigger, List<WeightedConditionalAction>> weightedEvents;
    protected final Set<Trigger> triggers = new HashSet<>();

    private int cooldown = -1;
    private boolean active = false;

    public ArtifactItem(Settings settings) {
        super(settings);
        attributeModifiers = new ArrayList<>();
        registerAttributeModifiers();
        events = new HashMap<>();
        weightedEvents = new HashMap<>();
    }

    public ArtifactItem cooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    private void actOnWeightedActions(TriggerData data) {
        ConditionData conditionData = new ConditionData(data);

        List<WeightedConditionalAction> actions = weightedEvents.getOrDefault(data.getData(DaggerKeys.TRIGGER), new ArrayList<>());
        actions = actions.stream()
                .filter(action -> action.getCondition().test(conditionData))
                .toList();

        if (actions.isEmpty())
            return;

        int totalWeight = actions
                .stream()
                .mapToInt(WeightedConditionalAction::getWeight)
                .reduce(0, Integer::sum);

        int currentWeight = 0;
        int random = (new Random()).nextInt(totalWeight);

        for (WeightedConditionalAction action : actions) {
            currentWeight += action.getWeight();
            if (currentWeight > random) {
                action.actOn(data);
                return;
            }
        }
    }

    public void receiveTrigger(TriggerData data) {
        DaggerLogger.debug("ArtifactItem: Received trigger {} for artifact {}", data.getData(DaggerKeys.TRIGGER).getName(), Registries.ITEM.getId(this));
        try {
            events.getOrDefault(data.getData(DaggerKeys.TRIGGER), new ArrayList<>()).forEach(a -> a.actOn(data));
            actOnWeightedActions(data);
        }
        catch (MissingDataException e) {
            DaggerLogger.error("ArtifactItem: Missing data for trigger {} action in artifact {}. Error: {}",
                    data.getData(DaggerKeys.TRIGGER).getName(),
                    Registries.ITEM.getId(this),
                    e.getMessage());
            throw e;
        }
    }

    public boolean hasTrigger(Trigger trigger) {
        return events.containsKey(trigger) || weightedEvents.containsKey(trigger);
    }

    public void registerAttributeModifiers() {
    }

    private void updatePlayer(ConditionData data) {
        for (ArtifactAttributeModifier attributeModifier : attributeModifiers) {
            try {
                attributeModifier.updatePlayer(data);
            } catch (MissingDataException e) {
                DaggerLogger.error("ArtifactItem: Missing data for attribute modifier update in artifact {}. Error: {}",
                        Registries.ITEM.getId(this),
                        e.getMessage());
                throw e;
            }
        }
    }

    private void cleansePlayer(ConditionData data) {
        for (ArtifactAttributeModifier attributeModifier : attributeModifiers) {
            try {
                attributeModifier.cleansePlayer(data);
            } catch (MissingDataException e) {
                DaggerLogger.error("ArtifactItem: Missing data for attribute modifier cleansing in artifact {}. Error: {}",
                        Registries.ITEM.getId(this),
                        e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.onEquip(stack, slot, entity);
        if (entity instanceof PlayerEntity player) {
            updatePlayer(new ConditionData().addData(DaggerKeys.PLAYER, player));
            for (Trigger trigger : triggers)
                trigger.addListener(player);
        }
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);
        if (entity instanceof PlayerEntity player)
            updatePlayer(new ConditionData().addData(DaggerKeys.PLAYER, player));
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.onUnequip(stack, slot, entity);
        if (entity instanceof PlayerEntity player) {
            cleansePlayer(new ConditionData().addData(DaggerKeys.PLAYER, player));
            for (Trigger trigger : triggers)
                trigger.removeListener(player);
        }
    }

    public ArtifactItem rarity(ArtifactRarity artifactRarity) {
        this.artifactRarity = artifactRarity;
        return this;
    }

    private void addArtifactRarity(List<Text> tooltip) {
        switch (artifactRarity) {
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
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (TrinketsUtil.hasArtifact(entity, stack.getItem())) {
            return false;
        }
        return super.canEquip(stack, slot, entity);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        addArtifactRarity(tooltip);
        if (Screen.hasShiftDown()) {
            if (stack.getItem() instanceof ArtifactItem artifact && artifact.isActive()) {
                tooltip.add(Text.translatable("item.magpie.tooltip.recharge", artifact.getCooldown() / 20 + " "));
            }
            TextFormatter.addTooltips(Text.translatable("item.magpie.tooltip." + stack.getItem()), tooltip);
        } else {
            if (stack.getItem() instanceof ArtifactItem artifact && artifact.isActive())
                TextFormatter.addTooltips(Text.translatable("item.magpie.tooltip.active"), tooltip);
            TextFormatter.addTooltips(Text.translatable("item.magpie.description." + stack.getItem()), tooltip);
            tooltip.add(Text.translatable("item.magpie.tooltip.shiftmoreinfo"));
        }
    }

    public int getCooldown() {
        return cooldown;
    }

    public boolean isActive() {
        return active;
    }

    public ArtifactItem active(boolean active) {
        this.active = active;
        return this;
    }

    public Identifier getIdentifier() {
        return Registries.ITEM.getId(this);
    }
}
