package xspleet.daggerapi.artifact.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.handler.logging.LogLevel;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.modifier.AttributeModifier;
import xspleet.daggerapi.attributes.modifier.DaggerAttributeModifier;
import xspleet.daggerapi.attributes.operations.AttributeOperation;
import xspleet.daggerapi.base.*;
import xspleet.daggerapi.collections.ConditionProviders;
import xspleet.daggerapi.collections.Triggers;
import xspleet.daggerapi.collections.registration.Mapper;
import xspleet.daggerapi.data.ProviderData;
import xspleet.daggerapi.data.key.DaggerKeys;
import xspleet.daggerapi.exceptions.*;
import xspleet.daggerapi.models.*;
import xspleet.daggerapi.trigger.Trigger;
import xspleet.daggerapi.trigger.actions.ConditionalAction;
import xspleet.daggerapi.trigger.actions.WeightedConditionalAction;

public class ArtifactItemBuilder
{
    public static ArtifactItem build(ItemModel itemModel)
    {
        String name = itemModel.getName();
        if(name == null || name.isEmpty()) {
            DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item at {} : {}", "Name", "Artifact must have a name");
            return null;
        }
        ArtifactRarity rarity = itemModel.getRarity();
        if(rarity == null) {
            DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", name, "Rarity", "Artifact must have a rarity");
        }
        boolean active = itemModel.isActive();
        int cooldown = itemModel.getCooldown();

        BuildableArtifactItem item = new BuildableArtifactItem(new FabricItemSettings().maxCount(1));

        if(active && cooldown <= 0) {
            DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", name, "Cooldown", "Active artifact must have a cooldown greater than 0");
        }
        if(!active && cooldown > 0) {
            DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", name, "Cooldown", "Inactive artifact must have no cooldown");
        }

        if(active) {
            item.cooldown(cooldown).active(true);
        }

        item.rarity(rarity);
        buildArtifactAttributes(itemModel, item);
        buildEvents(itemModel, item);

        return item;
    }

    private static void buildArtifactAttributes(ItemModel itemModel, BuildableArtifactItem item)
    {
        var artifactModifierModels = itemModel.getAttributeModifiers();
        for(int i = 0 ; i < artifactModifierModels.size() ; i++)
        {
            var artifactModifierModel = artifactModifierModels.get(i);

            var artifactAttributeModifier = new ArtifactAttributeModifier();

            var conditionsModels = artifactModifierModel.getConditions();
            var modifierModels = artifactModifierModel.getModifiers();

            for(int j = 0 ; j < conditionsModels.size() ; j++)
            {
                var conditionModel = conditionsModels.get(j);
                    if(conditionModel.getOn() != On.SELF && conditionModel.getOn() != On.WORLD)
                        DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("AttributeModifier", i, "Condition", j), "Condition on " + conditionModel.getOn() + " is not supported for artifact modifiers");
                try {
                    Condition conditionUnit = getCondition(conditionModel);
                    artifactAttributeModifier.addCondition(conditionUnit, conditionModel.getCondition());
                }
                catch (DaggerAPIException e) {
                    DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("AttributeModifier", i, "Condition", j), e.getMessage());
                }
            }

            for(int j = 0 ; j < modifierModels.size() ; j++)
            {
                var modifierModel = modifierModels.get(j);
                try {
                    String name = itemModel.getName() + "/" + modifierModel.getAttribute() + "/" + modifierModel.getModificationType() + "/" + modifierModel.getModificationValue().getAsString() + "/" + j + "/" + i;
                    artifactAttributeModifier.addAttributeModifier(
                            getModifier(name, itemModel.getName(), modifierModel)
                    );
                }
                catch (DaggerAPIException e) {
                    DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("AttributeModifier", i, "Modifier", j), e.getMessage());
                }
            }

            item.addAttributeModifier(artifactAttributeModifier);
        }
    }

    private static void buildEvents(ItemModel itemModel, BuildableArtifactItem item)
    {
        var events = itemModel.getEvents();
        if(itemModel.isActive() && events.stream().noneMatch(e -> e.getTrigger().equalsIgnoreCase(Triggers.ACTIVATE.getName()))) {
            DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), "Events", "Active artifact must have an activate event");
        }
        for(int i = 0 ; i < events.size() ; i++)
        {
            var eventModel = events.get(i);
            var conditionalAction = new ConditionalAction();

            if(eventModel.getWeight() != null) {
                conditionalAction = new WeightedConditionalAction(eventModel.getWeight());
            }

            Trigger trigger = null;
            try {
                trigger = Mapper.getTrigger(eventModel.getTrigger());
            }
            catch (NoSuchTriggerException e) {
                DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i), e.getMessage());
                continue;
            }

            if (trigger == Triggers.ACTIVATE) {
                if(!itemModel.isActive()) {
                    DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i), "Activate event cannot be set on an inactive artifact");
                    continue;
                }
            }

            TriggeredBy triggeredBy = eventModel.getTriggeredBy();
            TriggeredIn triggeredIn = eventModel.getTriggeredIn();

            if(triggeredBy != null && !trigger.hasTriggerer()) {
                DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i), "TriggeredBy is set on event " + eventModel.getTrigger() + " but the trigger does not provide a triggerer");
            }
            if(triggeredIn != null && !trigger.isWorldful()) {
                DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i), "TriggeredIn is set on event " + eventModel.getTrigger() + " but the trigger is not worldful");
            }

            conditionalAction
                    .triggeredIn(triggeredIn)
                    .triggeredBy(triggeredBy);

            var conditionsModels = eventModel.getConditions();
            for(int j = 0 ; j < conditionsModels.size() ; j++)
            {
                var conditionModel = conditionsModels.get(j);
                if(conditionModel.getOn() == On.SELF)
                    DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i, "Condition", j), "Condition on SELF is not supported for events. If on is missing, it defaults to SELF!");
                if(!trigger.hasTriggerer() && conditionModel.getOn() == On.TRIGGERER) {
                    DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i, "Condition", j), "Condition on TRIGGERER but the trigger does not provide a triggerer");
                }
                if(!trigger.isWorldful() && conditionModel.getOn() == On.WORLD) {
                    DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i, "Condition", j), "Condition on WORLD but the trigger is not worldful");
                }
                try {
                    Condition conditionUnit = getCondition(conditionModel);
                    conditionalAction.addCondition(conditionUnit, conditionModel.getCondition());
                }
                catch (DaggerAPIException e) {
                    DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i, "Condition", j), e.getMessage());
                }
            }

            var actionsModels = eventModel.getActions();
            for(int j = 0 ; j < actionsModels.size() ; j++)
            {
                var actionModel = actionsModels.get(j);
                try {
                    var actionProvider = Mapper.getActionProvider(actionModel.getAction());
                    if(actionProvider.isModifier() && actionModel.getOn() != On.TRIGGERER) {
                        DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i, "Action", j), "Action " + actionModel.getAction() + " is a modifier but is not on TRIGGERER, which is not supported");
                    }
                    if(!actionProvider.canBeOnTrigger(trigger)) {
                        DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i, "Action", j), "Action " + actionModel.getAction() + " cannot be used on trigger " + trigger.getName());
                    }
                    if(!trigger.hasTriggerer() && actionModel.getOn() == On.TRIGGERER) {
                        DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i, "Action", j), "Action " + actionModel.getAction() + " is on TRIGGERER but the trigger does not provide a triggerer");
                    }
                    if(!trigger.isWorldful() && actionModel.getOn() == On.WORLD) {
                        DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i, "Action", j), "Action " + actionModel.getAction() + " is on WORLD but the trigger is not worldful");
                    }
                    var actionUnit = actionProvider.provide(actionModel.getOn(), actionModel.getArguments());
                    conditionalAction.addAction(actionUnit, actionModel.getAction());
                }
                catch (DaggerAPIException e) {
                    DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i, "Action", j), e.getMessage());
                }
            }

            if(trigger == Triggers.ACTIVATE)
            {
                if(conditionsModels.stream().map(x -> x.getCondition()).noneMatch(n -> n.equalsIgnoreCase("isArtifact") || n.equalsIgnoreCase("not isArtifact"))) {
                    try {
                        conditionalAction.addCondition(
                                ConditionProviders.IF_ARTIFACT
                                        .provide(new ProviderData()
                                                .setOn(On.TRIGGERER)
                                                .addData(DaggerKeys.Provider.ARTIFACT, itemModel.getName())),
                                "ifArtifact"
                        );
                        DaggerLogger.warn(LoggingContext.PARSING, "Artifact {} has no ifArtifact condition on activate event, adding it automatically.", itemModel.getName());
                    } catch (BadArgumentsException e) {
                        DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i), "Missing ifArtifact condition on active artifact.");
                    }
                }

                if(conditionsModels.stream().map(x -> x.getCondition()).noneMatch(n -> n.equalsIgnoreCase("isSuccessful") || n.equalsIgnoreCase("not isSuccessful"))) {
                    try {
                        conditionalAction.addCondition(
                                ConditionProviders.IF_SUCCESSFUL
                                        .provide(new ProviderData().setOn(On.TRIGGERER)),
                                "isSuccessful"
                        );
                        DaggerLogger.warn(LoggingContext.PARSING,"Artifact {} has no isSuccessful condition on activate event, adding it automatically.", itemModel.getName());
                    } catch (BadArgumentsException e) {
                        DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i), "Missing isSuccessful condition on active artifact.");
                    }
                }
            }

            item.addEvent(trigger, conditionalAction);
        }
    }

    private static WrappedModifier<?> getModifier(String name, String artifactName, AttributeModifierModel modifierModel) throws NoSuchAttributeException, NoSuchOperationException, ParseException {
        String attributeName = modifierModel.getAttribute();
        String operationName = modifierModel.getModificationType();
        JsonElement value = modifierModel.getModificationValue();

        AttributeOperation<?> operation = Mapper.getOperation(operationName);
        Attribute<?> attribute = Mapper.getAttribute(attributeName);

        AttributeModifier<?> modifier = safeCreateModifier(name, attribute, value, operation, artifactName);

        return new WrappedModifier(
                attribute,
                modifier
        );
    }

    private static Condition getCondition(ConditionModel conditionModel) throws NoSuchConditionException, BadArgumentsException {
        String condition = conditionModel.getCondition();
        var splitCondition = condition.split(" ");
        boolean negate = false;
        String conditionName;
        if(splitCondition.length == 1) {
            conditionName = splitCondition[0];
        }
        else if(splitCondition.length == 2) {
            if(!splitCondition[0].equalsIgnoreCase("not"))
                throw new NoSuchConditionException(condition);
            negate = true;
            conditionName = splitCondition[1];
        }
        else {
            throw new NoSuchConditionException(condition);
        }

        var conditionProvider = Mapper.getConditionProvider(conditionName);
        var conditionUnit = conditionProvider.provide(conditionModel.getOn(), conditionModel.getArguments());
        return negate ? conditionUnit.negate() : conditionUnit;
    }

    private static <T> AttributeModifier<T> safeCreateModifier(
            String name,
            Attribute<?> attribute,
            JsonElement value,
            AttributeOperation<?> operation,
            String artifactName
    ) throws ParseException {
        if(value == null || value.isJsonNull())
            throw new IllegalArgumentException("Value for attribute modifier cannot be null or JSON null");

        if(!(value instanceof JsonPrimitive primitive))
            throw new IllegalArgumentException("Value for attribute modifier must be a JSON primitive");

        var castOperation = (AttributeOperation<T>) operation;

        T castValue;
        if(castOperation.getType().equals(Integer.class))
        {
            if(!primitive.isNumber())
                throw new ParseException("Value for integer attribute modifier must be a integer");
            castValue = castOperation.getType().cast(primitive.getAsInt());
        }
        else if(castOperation.getType().equals(Double.class))
        {
            if(!primitive.isNumber())
                throw new ParseException("Value for double attribute modifier must be a double");
            castValue = castOperation.getType().cast(primitive.getAsDouble());
        }
        else if(castOperation.getType().equals(Boolean.class))
        {
            if(!primitive.isBoolean())
                throw new ParseException("Value for boolean attribute modifier must be a boolean");
            castValue = castOperation.getType().cast(primitive.getAsBoolean());
        }
        else {
            throw new ParseException("Unsupported attribute modifier type: " + castOperation.getType());
        }

        return new DaggerAttributeModifier<>(
                name,
                castValue,
                castOperation,
                artifactName
        );
    }
}
