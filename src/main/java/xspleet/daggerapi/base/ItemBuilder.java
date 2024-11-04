package xspleet.daggerapi.base;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.base.actions.ConditionalAction;
import xspleet.daggerapi.base.actions.WeightedConditionalAction;
import xspleet.daggerapi.base.artifact.BuildableArtifactItem;
import xspleet.daggerapi.models.*;

import java.util.List;

public class ItemBuilder
{
    public static Item build(ItemModel itemModel)
    {
        String name = itemModel.name;
        String rarity = itemModel.rarity;
        boolean active = itemModel.active;
        int cooldown = itemModel.cooldown;

        BuildableArtifactItem item = new BuildableArtifactItem(new FabricItemSettings().maxCount(1));

        if(active)
            item.cooldown(cooldown);
        item.rarity(ArtifactRarity.getRarity(rarity));

        buildArtifactAttributes(item, itemModel.attributeModifiers);
        buildEvents(item, itemModel.events);

        return Registry.register(Registries.ITEM, Identifier.of(DaggerAPI.MOD_ID, name), item);
    }

    private static void buildArtifactAttributes(BuildableArtifactItem item, List<ArtifactAttributeModifierModel> modifierModels)
    {
        int i = 0;
        for(ArtifactAttributeModifierModel modifierModel: modifierModels)
        {
            ArtifactAttributeModifier artifactAttributeModifier = new ArtifactAttributeModifier();
            for(ConditionModel conditionModel: modifierModel.conditions)
            {
                Condition condition = Mapper
                        .getConditionProvider(conditionModel.condition)
                        .provide(conditionModel.arguments);

                artifactAttributeModifier.addCondition(condition);
            }
            for(AttributeModifierModel attributeModifierModel: modifierModel.modifiers)
            {
                int j = 0;
                EntityAttribute attribute = Mapper.getEntityAttribute(attributeModifierModel.attribute);
                EntityAttributeModifier modifier = new EntityAttributeModifier(
                        i + "/" + j + "/" + attributeModifierModel.attribute + "/" + attributeModifierModel.modificationType + "/" + attributeModifierModel.modificationValue,
                        attributeModifierModel.modificationValue,
                        Mapper.getOperation(attributeModifierModel.modificationType)
                );

                artifactAttributeModifier.addAttributeModifier(
                        new DaggerAttributeModifier(
                                attribute,
                                modifier
                        )
                );
            }
            item.addAttributeModifier(artifactAttributeModifier);
        }
    }

    private static void buildEvents(BuildableArtifactItem item, List<EventModel> eventModels)
    {
        for(EventModel eventModel: eventModels)
        {
            Trigger trigger = Mapper.getTrigger(eventModel.trigger);

            ConditionalAction action;
            if(eventModel.weight != null)
                action = new WeightedConditionalAction(eventModel.weight);
            else
                action = new ConditionalAction();

            for(ConditionModel conditionModel: eventModel.conditions)
            {
                var condition = Mapper
                        .getConditionProvider(conditionModel.condition)
                        .provide(conditionModel.arguments);

                action.addCondition(condition);
            }

            for(ActionModel actionModel: eventModel.actions)
            {
                var unitAction = Mapper
                        .getActionProvider(actionModel.action)
                        .provide(actionModel.arguments);

                action.addAction(unitAction);
            }

            item.addEvent(trigger, action);
        }
    }
}
