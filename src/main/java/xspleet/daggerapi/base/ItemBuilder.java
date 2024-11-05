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
import xspleet.daggerapi.exceptions.*;
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

        buildArtifactAttributes(itemModel, item);
        buildEvents(itemModel, item);

        return Registry.register(Registries.ITEM, Identifier.of(DaggerAPI.MOD_ID, name), item);
    }

    private static void buildArtifactAttributes(ItemModel itemModel, BuildableArtifactItem item)
    {
        var modifierModels = itemModel.attributeModifiers;
        int i = 0;
        for(ArtifactAttributeModifierModel modifierModel: modifierModels)
        {
            ArtifactAttributeModifier artifactAttributeModifier = new ArtifactAttributeModifier();
            for(ConditionModel conditionModel: modifierModel.conditions)
            {
                try {
                    Condition condition = Mapper
                            .getConditionProvider(conditionModel.condition)
                            .provide(conditionModel.arguments);

                    artifactAttributeModifier.addCondition(condition);
                }
                catch(NoSuchConditionException conditionException)
                {
                    conditionModel.condition = "HERE --- " + conditionModel.condition + " ---";
                    ErrorLogger.log(itemModel.name, conditionException, modifierModel);
                }
            }
            int j = 0;
            for(AttributeModifierModel attributeModifierModel: modifierModel.modifiers)
            {
                EntityAttribute attribute = null;
                try{
                    attribute = Mapper.getEntityAttribute(attributeModifierModel.attribute);
                }
                catch(NoSuchAttributeException attributeException)
                {
                    attributeModifierModel.attribute = "HERE --- " + attributeModifierModel.attribute + " ---";
                    ErrorLogger.log(itemModel.name, attributeException, modifierModel);
                }
                try {
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
                catch(NoSuchOperationException operationException)
                {
                    attributeModifierModel.modificationType = "HERE --- " + attributeModifierModel.modificationType + " ---";
                    ErrorLogger.log(itemModel.name, operationException, modifierModel);
                }
                j++;
            }
            i++;
            item.addAttributeModifier(artifactAttributeModifier);
        }
    }

    private static void buildEvents(ItemModel itemModel, BuildableArtifactItem item)
    {
        var eventModels = itemModel.events;
        for(EventModel eventModel: eventModels)
        {
            ConditionalAction action;
            if (eventModel.weight != null)
                action = new WeightedConditionalAction(eventModel.weight);
            else
                action = new ConditionalAction();

            for (ConditionModel conditionModel : eventModel.conditions) {
                try{
                    var condition = Mapper
                            .getConditionProvider(conditionModel.condition)
                            .provide(conditionModel.arguments);

                    action.addCondition(condition);
                }
                catch(NoSuchConditionException conditionException)
                {
                    conditionModel.condition = "HERE! --- " + conditionModel.condition + " ---";
                    ErrorLogger.log(itemModel.name, conditionException, eventModel);
                }
            }

            for (ActionModel actionModel : eventModel.actions) {
                try{
                    var unitAction = Mapper
                            .getActionProvider(actionModel.action)
                            .provide(actionModel.arguments);

                    action.addAction(unitAction);
                }
                catch(NoSuchActionException actionException)
                {
                    actionModel.action = "HERE! --- " + actionModel.action + " ---";
                    ErrorLogger.log(itemModel.name, actionException, eventModel);
                }
            }
            try {
                Trigger trigger = Mapper.getTrigger(eventModel.trigger);
                item.addEvent(trigger, action);
            }
            catch (NoSuchTriggerException triggerException)
            {
                eventModel.trigger = "HERE --- " + eventModel.trigger + " ---";
                ErrorLogger.log(itemModel.name, triggerException, eventModel);
            }
        }
    }
}
