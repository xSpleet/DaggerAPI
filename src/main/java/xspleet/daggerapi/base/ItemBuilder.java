package xspleet.daggerapi.base;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import xspleet.jdagapi.Jdagapi;
import xspleet.jdagapi.base.actions.ConditionalAction;
import xspleet.jdagapi.base.actions.WeightedConditionalAction;
import xspleet.jdagapi.base.artifact.BuildableArtifactItem;
import xspleet.jdagapi.collections.ConditionProviders;
import xspleet.jdagapi.collections.Triggers;
import xspleet.jdagapi.models.*;
import xspleet.jdagapi.providers.ActionProvider;
import xspleet.jdagapi.providers.ConditionProvider;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class ItemBuilder
{
    private ItemModel itemModel;
    private BuildableArtifactItem item;
    private Item ITEM;

    public ItemBuilder(ItemModel itemModel)
    {
        this.itemModel = itemModel;
        item = new BuildableArtifactItem(new FabricItemSettings().maxCount(1));
    }

    public Item build()
    {
        buildAttributeModifiers();
        buildEvents();
        ITEM = registerItem(ArtifactRarity.getRarity(itemModel.rarity), itemModel.cooldown);
        return ITEM;
    }

    public Item registerItem(ArtifactRarity rarity, int cooldown)
    {
        return registerItem(itemModel.name,
                item.rarity(rarity).cooldown(cooldown));
    }

    private static Item registerItem(String name, Item item)
    {
        return Registry.register(Registries.ITEM, new Identifier(Jdagapi.MOD_ID, name), item);
    }

    private void buildAttributeModifiers()
    {
        int i = 0;
        for(ArtifactAttributeModifierModel artifactAttributeModifierModel: itemModel.attributeModifiers) {
            Predicate<PlayerEntity> condition = ConditionProviders.ALWAYS.provide(new HashMap<>());
            for (ConditionModel conditionModel : artifactAttributeModifierModel.conditions)
                condition = condition.and(ConditionProvider.getConditionFromString(stringCondition));

            int j = 0;
            for(AttributeModifierModel attributeModifierModel: artifactAttributeModifierModel.modifiers)
            {
                EntityAttribute attribute = Mapper.getEntityAttribute(attributeModifierModel.attribute);
                EntityAttributeModifier.Operation operation = Mapper.getOperation(attributeModifierModel.modificationType);
                double value = attributeModifierModel.modificationValue;
                EntityAttributeModifier entityAttributeModifier = new EntityAttributeModifier(
                        itemModel.name + "/" + i + "/" + j + "/" + attributeModifierModel.attribute,
                        value,
                        operation
                );

                ArtifactAttributeModifier artifactAttributeModifier = new ArtifactAttributeModifier()
                        .setModifier(entityAttributeModifier)
                        .setAttribute(attribute)
                        .setCondition(condition);

                item.addAttributeModifier(artifactAttributeModifier);
                j++;
            }
            i++;
        }
    }

    private void buildEvents()
    {
        List<EventModel> eventModels = itemModel.events;
        for(EventModel eventModel: eventModels)
        {
            Trigger trigger = Triggers.getByName(eventModel.trigger);

            ConditionalAction action;
            if(eventModel.weight != 0)
                action = new WeightedConditionalAction(eventModel.weight);
            else
                action = new ConditionalAction();

            if(eventModel.conditions != null)
                for(String condition: eventModel.conditions)
                    action.addCondition(ConditionProvider.getConditionFromString(condition));
            for(String actionString: eventModel.actions)
                action.addAction(ActionProvider.getActionFromString(actionString));

            item.addEvent(trigger, action);
        }
    }
}
