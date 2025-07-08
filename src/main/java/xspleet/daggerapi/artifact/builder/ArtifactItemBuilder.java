package xspleet.daggerapi.artifact.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.w3c.dom.Attr;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.modifier.AttributeModifier;
import xspleet.daggerapi.attributes.modifier.DaggerAttributeModifier;
import xspleet.daggerapi.attributes.operations.AttributeOperation;
import xspleet.daggerapi.base.*;
import xspleet.daggerapi.collections.Triggers;
import xspleet.daggerapi.data.ProviderData;
import xspleet.daggerapi.base.Condition;
import xspleet.daggerapi.models.On;
import xspleet.daggerapi.trigger.actions.ConditionalAction;
import xspleet.daggerapi.trigger.Trigger;
import xspleet.daggerapi.trigger.actions.WeightedConditionalAction;
import xspleet.daggerapi.collections.registration.Mapper;
import xspleet.daggerapi.exceptions.*;
import xspleet.daggerapi.models.*;

public class ArtifactItemBuilder
{
    public static Item build(ItemModel itemModel)
    {
        String name = itemModel.getName();
        String rarity = itemModel.getRarity();
        boolean active = itemModel.isActive();
        int cooldown = itemModel.getCooldown();

        BuildableArtifactItem item = new BuildableArtifactItem(new FabricItemSettings().maxCount(1));

        if(active)
        {
            item.cooldown(cooldown)
                    .active(true);
        }

        item.rarity(ArtifactRarity.getRarity(rarity));

        buildArtifactAttributes(itemModel, item);
        buildEvents(itemModel, item);

        return Registry.register(Registries.ITEM, Identifier.of(DaggerAPI.MOD_ID, name), item);
    }

    private static <T> AttributeModifier<T> safeCreateModifier(
            String name,
            JsonElement value,
            AttributeOperation<?> operation
    )
    {
        if(value == null || value.isJsonNull())
            throw new IllegalArgumentException("Value for attribute modifier cannot be null or JSON null");

        if(!(value instanceof JsonPrimitive primitive))
            throw new IllegalArgumentException("Value for attribute modifier must be a JSON primitive");

        var castOperation = (AttributeOperation<T>) operation;

        T castValue;
        if(castOperation.getType().equals(Integer.class))
        {
            if(!primitive.isNumber())
                throw new IllegalArgumentException("Value for integer attribute modifier must be a number");
            castValue = (T) Integer.valueOf(primitive.getAsInt());
        }
        else if(castOperation.getType().equals(Double.class))
        {
            if(!primitive.isNumber())
                throw new IllegalArgumentException("Value for double attribute modifier must be a number");
            castValue = (T) Double.valueOf(primitive.getAsDouble());
        }
        else if(castOperation.getType().equals(Boolean.class))
        {
            if(!primitive.isBoolean())
                throw new IllegalArgumentException("Value for boolean attribute modifier must be a boolean");
            castValue = (T) Boolean.valueOf(primitive.getAsBoolean());
        }
        else
        {
            throw new IllegalArgumentException("Unsupported attribute modifier type: " + castOperation.getType());
        }

        return new DaggerAttributeModifier<>(
                name,
                castValue,
                castOperation
        );
    }

    private static void buildArtifactAttributes(ItemModel itemModel, BuildableArtifactItem item)
    {
        var modifierModels = itemModel.getAttributeModifiers();
        int i = 0;
        for(ArtifactAttributeModifierModel modifierModel: modifierModels)
        {
            ArtifactAttributeModifier artifactAttributeModifier = new ArtifactAttributeModifier();
            for(ConditionModel conditionModel: modifierModel.getConditions())
            {
                try {
                    Condition condition = Mapper
                            .getConditionProvider(conditionModel.getCondition())
                            .provide(new ProviderData(conditionModel.getArguments())
                                    .setOn(conditionModel.getOn()));

                    if(conditionModel.getOn() != On.SELF && conditionModel.getOn() != On.WORLD)
                        throw new NoSuchConditionException("Condition " + conditionModel.getCondition() + " is applied to self or world when used in attribute modifiers, but on is set to " + conditionModel.getOn());

                    artifactAttributeModifier.addCondition(condition);
                }
                catch(NoSuchConditionException conditionException)
                {
                    conditionModel.setCondition("HERE --- " + conditionModel.getCondition() + " ---");
                    ErrorLogger.log(itemModel.getName(), conditionException, modifierModel);
                }
            }
            int j = 0;
            for(AttributeModifierModel attributeModifierModel: modifierModel.getModifiers())
            {
                Attribute<?> attribute = null;
                try{
                    attribute = Mapper.getEntityAttribute(attributeModifierModel.getAttribute());
                }
                catch(NoSuchAttributeException attributeException)
                {
                    attributeModifierModel.setAttribute("HERE --- " + attributeModifierModel.getAttribute() + " ---");
                    ErrorLogger.log(itemModel.getName(), attributeException, modifierModel);
                }
                try {
                    AttributeModifier<?> modifier = safeCreateModifier(
                            i + "/" + j + "/" + attributeModifierModel.getAttribute() + "/" + attributeModifierModel.getModificationType() + "/" + attributeModifierModel.getModificationValue(),
                            attributeModifierModel.getModificationValue(),
                            Mapper.getOperation(attributeModifierModel.getModificationType())
                    );

                    artifactAttributeModifier.addAttributeModifier(
                            new WrappedModifier(
                                    attribute,
                                    modifier
                            )
                    );
                }
                catch(NoSuchOperationException operationException)
                {
                    attributeModifierModel.setModificationType("HERE --- " + attributeModifierModel.getModificationType() + " ---");
                    ErrorLogger.log(itemModel.getName(), operationException, modifierModel);
                }
                j++;
            }
            i++;
            item.addAttributeModifier(artifactAttributeModifier);
        }
    }

    private static void buildEvents(ItemModel itemModel, BuildableArtifactItem item)
    {
        var eventModels = itemModel.getEvents();
        for(EventModel eventModel: eventModels)
        {
            Trigger trigger = Mapper.getTrigger(eventModel.getTrigger());
            ConditionalAction action;
            if (eventModel.getWeight() != null)
                action = new WeightedConditionalAction(eventModel.getWeight());
            else
                action = new ConditionalAction();

            action.triggeredIn(eventModel.getTriggeredIn())
                    .triggeredBy(eventModel.getTriggeredBy());

            for (ConditionModel conditionModel : eventModel.getConditions()) {
                try{
                    var condition = Mapper
                            .getConditionProvider(conditionModel.getCondition())
                            .provide(new ProviderData(conditionModel.getArguments())
                                    .setOn(conditionModel.getOn()));

                    if(conditionModel.getOn() == On.SELF)
                        throw new NoSuchConditionException("Condition " + conditionModel.getCondition() + " is not applied to self in events, but on is set to " + conditionModel.getOn());

                    action.addCondition(condition);
                }
                catch(NoSuchConditionException conditionException)
                {
                    conditionModel.setCondition("HERE! --- " + conditionModel.getCondition() + " ---");
                    ErrorLogger.log(itemModel.getName(), conditionException, eventModel);
                }
            }

            for (ActionModel actionModel : eventModel.getActions()) {
                try{
                    var unitAction = Mapper
                            .getActionProvider(actionModel.getAction())
                            .provide(new ProviderData(actionModel.getArguments())
                                    .setOn(actionModel.getOn()));

                    if(actionModel.getOn() == On.SELF)
                        throw new NoSuchActionException("Action " + actionModel.getAction() + " is not applied to self in events, but on is set to " + actionModel.getOn());

                    action.addAction(unitAction);
                }
                catch(NoSuchActionException actionException)
                {
                    actionModel.setAction("HERE! --- " + actionModel.getAction() + " ---");
                    ErrorLogger.log(itemModel.getName(), actionException, eventModel);
                }
            }
            try {
                if(!trigger.hasTriggerer() && eventModel.getTriggeredBy() != null)
                {
                    throw new NoSuchTriggerException("Trigger " + eventModel.getTrigger() + " does not provide a triggerer, but triggered by is set to " + eventModel.getTriggeredBy());
                }
                if(!trigger.isWorldful() && eventModel.getTriggeredIn() != null)
                {
                    throw new NoSuchTriggerException("Trigger " + eventModel.getTrigger() + " is not worldful, but triggered in is set to " + eventModel.getTriggeredIn());
                }

                if(trigger == Triggers.ACTIVATE
                        && eventModel.getConditions().stream().anyMatch(x -> x.getCondition().equalsIgnoreCase("isArtifact"))
                        && eventModel.getConditions().stream().anyMatch(x -> x.getCondition().equalsIgnoreCase("isSuccessful")))
                {
                    action.addCondition(
                            Mapper.getConditionProvider("isArtifact")
                                    .provide(new ProviderData().setOn(On.TRIGGERER)
                                            .addData("artifact", "choose daggerapi:" + itemModel.getName()))
                    );

                    action.addCondition(
                            Mapper.getConditionProvider("isSuccessful")
                                    .provide(new ProviderData().setOn(On.TRIGGERER)
                                            .addData("successful", "true"))
                    );
                }

                item.addEvent(trigger, action);
            }
            catch (NoSuchTriggerException triggerException)
            {
                eventModel.setTrigger("HERE --- " + eventModel.getTrigger() + " ---");
                ErrorLogger.log(itemModel.getName(), triggerException, eventModel);
            }
        }
    }
}
