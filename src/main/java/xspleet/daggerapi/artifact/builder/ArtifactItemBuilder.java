package xspleet.daggerapi.artifact.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.handler.logging.LogLevel;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.api.logging.LoggingContext;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.artifact.ArtifactRarity;
import xspleet.daggerapi.artifact.modifiers.ArtifactAttributeModifier;
import xspleet.daggerapi.artifact.modifiers.WrappedModifier;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.modifier.AttributeModifier;
import xspleet.daggerapi.attributes.modifier.DaggerAttributeModifier;
import xspleet.daggerapi.attributes.operations.AttributeOperation;
import xspleet.daggerapi.api.collections.ConditionProviders;
import xspleet.daggerapi.api.collections.Triggers;
import xspleet.daggerapi.api.registration.Mapper;
import xspleet.daggerapi.data.collection.ProviderData;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.api.collections.DaggerKeys;
import xspleet.daggerapi.exceptions.*;
import xspleet.daggerapi.api.models.*;
import xspleet.daggerapi.trigger.Condition;
import xspleet.daggerapi.trigger.Trigger;
import xspleet.daggerapi.trigger.actions.ConditionalAction;
import xspleet.daggerapi.trigger.actions.WeightedConditionalAction;

import java.util.List;
import java.util.Set;

public class ArtifactItemBuilder
{
    public static ArtifactItem build(ItemModel itemModel, Identifier id)
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
        buildArtifactAttributes(itemModel, item, id);
        buildEvents(itemModel, item, id);

        return item;
    }

    private static void buildArtifactAttributes(ItemModel itemModel, BuildableArtifactItem item, Identifier id)
    {
        var artifactModifierModels = itemModel.getAttributeModifiers();
        Set<DaggerKey<?>> availableData = Set.of(DaggerKeys.PLAYER, DaggerKeys.WORLD);
        for(int i = 0 ; i < artifactModifierModels.size() ; i++)
        {
            var artifactModifierModel = artifactModifierModels.get(i);

            var artifactAttributeModifier = new ArtifactAttributeModifier();

            var conditionsModels = artifactModifierModel.getConditions();
            var modifierModels = artifactModifierModel.getModifiers();

            for(int j = 0 ; j < conditionsModels.size() ; j++)
            {
                var conditionModel = conditionsModels.get(j);
                DaggerKey<?> onKey = null;
                try {
                    onKey = translateOn(conditionModel.getOn(), availableData);
                } catch (DaggerAPIException e) {
                    DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("AttributeModifier", i, "Condition", j), e.getMessage());
                }
                try {
                    Condition conditionUnit = getCondition(conditionModel, availableData, onKey);
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
                    String name = id.toString() + "/" + modifierModel.getAttribute() + "/" + modifierModel.getModificationType() + "/" + modifierModel.getModificationValue().getAsString() + "/" + j + "/" + i;
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

    private static void buildEvents(ItemModel itemModel, BuildableArtifactItem item, Identifier id)
    {
        var events = itemModel.getEvents();
        if(itemModel.isActive() && events.stream().noneMatch(e -> e.getTrigger().equalsIgnoreCase(Triggers.ACTIVATE.getName()))) {
            DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), "Events", "Active artifact must have an onActivate event");
        }
        for(int i = 0 ; i < events.size() ; i++)
        {
            var eventModel = events.get(i);
            var conditionalAction = new ConditionalAction();

            if(eventModel.getWeight() != null) {
                conditionalAction = new WeightedConditionalAction(eventModel.getWeight());
            }

            Trigger trigger;
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

            if(triggeredBy != null && !trigger.hasTriggerSource()) {
                DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i), "'source' is set on event " + eventModel.getTrigger() + " but the trigger does not provide a trigger source");
            }
            if(triggeredIn != null && !trigger.isWorldful()) {
                DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i), "'inWorld' is set on event " + eventModel.getTrigger() + " but the trigger is not worldful");
            }

            if(triggeredBy == null && trigger.hasTriggerSource()) {
                DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i), "'source' is not set on event " + eventModel.getTrigger() + ", but the trigger has a source");
            }
            if(triggeredIn == null && trigger.isWorldful()) {
                DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i), "'inWorld' is not set on event " + eventModel.getTrigger() + ", but the trigger has a world");
            }

            conditionalAction
                    .triggeredIn(triggeredIn)
                    .triggeredBy(triggeredBy);

            var conditionsModels = eventModel.getConditions();
            for(int j = 0 ; j < conditionsModels.size() ; j++)
            {
                var conditionModel = conditionsModels.get(j);
                DaggerKey<?> onKey = null;
                try {
                    onKey = translateOn(conditionModel.getOn(), trigger.getProvidedData());
                } catch (DaggerAPIException e) {
                    DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i, "Condition", j), e.getMessage());
                }
                try {
                    Condition conditionUnit = getCondition(conditionModel, trigger.getProvidedData(), onKey);
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
                DaggerKey<?> onKey = null;
                try {
                    onKey = translateOn(actionModel.getOn(), trigger.getProvidedData());
                } catch (DaggerAPIException e) {
                    DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i, "Action", j), e.getMessage());
                }
                try {
                    var actionProvider = Mapper.getActionProvider(actionModel.getAction());
                    if(!actionProvider.canBeOnTrigger(trigger)) {
                        DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i, "Action", j), "Action " + actionModel.getAction() + " cannot be used on trigger " + trigger.getName());
                    }
                    var data = actionProvider.readArgs(actionModel.getArguments()).setOn(onKey);

                    var requiredData = actionProvider.getRequiredData();
                    requiredData.addAll(data.getRequiredKeys());
                    if(!trigger.getProvidedData().containsAll(requiredData)) {
                        throw new MissingRequiredDataException(
                                requiredData.stream()
                                        .filter(k -> !trigger.getProvidedData().contains(k))
                                        .map(DaggerKey::key)
                                        .toList()
                        );
                    }
                    var actionUnit = actionProvider.provide(data);
                    conditionalAction.addAction(actionUnit, actionModel.getAction());
                }
                catch (DaggerAPIException e) {
                    DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i, "Action", j), e.getMessage());
                }
            }

            if(trigger == Triggers.ACTIVATE)
            {
                if(conditionsModels.stream().map(ConditionModel::getCondition).noneMatch(n -> n.equalsIgnoreCase("ifArtifact") || n.equalsIgnoreCase("not ifArtifact"))) {
                    try {
                        conditionalAction.addCondition(
                                ConditionProviders.IF_ARTIFACT
                                        .provide(new ProviderData()
                                                .setOn(DaggerKeys.TRIGGER_SOURCE)
                                                .addData(DaggerKeys.Provider.ARTIFACT, id)),
                                "ifArtifact"
                        );
                        DaggerLogger.warn(LoggingContext.PARSING, "Artifact {} has no ifArtifact condition on onActivate event, adding it automatically.", itemModel.getName());
                    } catch (BadArgumentsException e) {
                        DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} at {} : {}", itemModel.getName(), DaggerLogger.placeOf("Event", i), "Missing ifArtifact condition on active artifact.");
                    }
                }

                if(conditionsModels.stream().map(ConditionModel::getCondition).noneMatch(n -> n.equalsIgnoreCase("ifSuccessful") || n.equalsIgnoreCase("not ifSuccessful"))) {
                    try {
                        conditionalAction.addCondition(
                                ConditionProviders.IF_SUCCESSFUL
                                        .provide(new ProviderData().setOn(DaggerKeys.TRIGGER_SOURCE)),
                                "isSuccessful"
                        );
                        DaggerLogger.warn(LoggingContext.PARSING,"Artifact {} has no isSuccessful condition on onActivate event, adding it automatically.", itemModel.getName());
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

        AttributeModifier<?> modifier = safeCreateModifier(name, value, operation, artifactName);

        return new WrappedModifier(
                attribute,
                modifier
        );
    }

    private static Condition getCondition(ConditionModel conditionModel, Set<DaggerKey<?>> availableData, DaggerKey<?> onKey) throws NoSuchConditionException, BadArgumentsException, MissingRequiredDataException {
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
        var data = conditionProvider.readArgs(conditionModel.getArguments()).setOn(onKey);
        var requiredData = conditionProvider.getRequiredData();
        requiredData.addAll(data.getRequiredKeys());
        if(!availableData.containsAll(requiredData))
            throw new MissingRequiredDataException(
                    requiredData.stream()
                            .filter(k -> !availableData.contains(k))
                            .map(DaggerKey::key)
                            .toList()
            );
        var conditionUnit = conditionProvider.provide(data);
        return negate ? conditionUnit.negate() : conditionUnit;
    }

    private static <T> AttributeModifier<T> safeCreateModifier(
            String name,
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

    private static DaggerKey<?> translateOn(String on, Set<DaggerKey<?>> availableKeys) throws MissingRequiredDataException, BadArgumentException {
        var optionalKey = availableKeys.stream().filter((key) -> key.key().equals(on)).findFirst();
        var candidateKey = optionalKey.orElseThrow(() -> new MissingRequiredDataException(List.of(on)));
        if(Entity.class.isAssignableFrom(candidateKey.type()) || World.class.isAssignableFrom(candidateKey.type())) {
            return candidateKey;
        }
        else {
            throw new BadArgumentException("The 'on' argument should be either a world or an entity, but was: " + candidateKey.type().getSimpleName());
        }
    }
}
