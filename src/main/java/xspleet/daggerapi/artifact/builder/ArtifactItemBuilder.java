package xspleet.daggerapi.artifact.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.modifier.AttributeModifier;
import xspleet.daggerapi.attributes.modifier.DaggerAttributeModifier;
import xspleet.daggerapi.attributes.operations.AttributeOperation;
import xspleet.daggerapi.base.*;
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

        if(active) {
            item.cooldown(cooldown).active(true);
        }

        item.rarity(ArtifactRarity.getRarity(rarity));
        buildArtifactAttributes(itemModel, item);
        buildEvents(itemModel, item);

        return Registry.register(Registries.ITEM, Identifier.of(DaggerAPI.MOD_ID, name), item);
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
                try {
                    if(conditionModel.getOn() != On.SELF && conditionModel.getOn() != On.WORLD)
                        throw new WrongConditionOnException("Condition on " + conditionModel.getOn() + " is not supported for artifact modifiers");
                }
                catch (WrongConditionOnException e) {
                    ErrorLogger.log(itemModel.getName(), ErrorLogger.placeOf("ArtifactModifier", i, "Condition", j), e);
                    continue;
                }
                try {
                    Condition conditionUnit = getCondition(conditionModel);
                    artifactAttributeModifier.addCondition(conditionUnit);
                }
                catch (DaggerAPIException e) {
                    ErrorLogger.log(itemModel.getName(), ErrorLogger.placeOf("ArtifactModifier", i, "Condition", j), e);
                    continue;
                }
            }

            for(int j = 0 ; j < modifierModels.size() ; j++)
            {
                var modifierModel = modifierModels.get(j);
                try {
                    String name = itemModel.getName() + "_" + modifierModel.getAttribute() + "_" + modifierModel.getModificationType() + "_" + modifierModel.getModificationValue().getAsString() + "_" + j + "_" + i;
                    artifactAttributeModifier.addAttributeModifier(
                            getModifier(name, modifierModel)
                    );
                }
                catch (DaggerAPIException e) {
                    ErrorLogger.log(itemModel.getName(), ErrorLogger.placeOf("ArtifactModifier", i, "Modifier", j), e);
                    continue;
                }
            }

            item.addAttributeModifier(artifactAttributeModifier);
        }
    }

    private static void buildEvents(ItemModel itemModel, BuildableArtifactItem item)
    {

    }

    private static WrappedModifier<?> getModifier(String name, AttributeModifierModel modifierModel) throws NoSuchAttributeException, NoSuchOperationException {
        String attributeName = modifierModel.getAttribute();
        String operationName = modifierModel.getModificationType();
        JsonElement value = modifierModel.getModificationValue();

        AttributeOperation<?> operation = Mapper.getOperation(operationName);
        Attribute<?> attribute = Mapper.getAttribute(attributeName);

        AttributeModifier<?> modifier = safeCreateModifier(name, attribute, value, operation);

        return new WrappedModifier(
                attribute,
                modifier
        );
    }

    private static Condition getCondition(ConditionModel conditionModel) throws NoSuchConditionException, MissingArgumentException {
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
            AttributeOperation<?> operation
    ) {
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
            castValue = castOperation.getType().cast(primitive.getAsInt());
        }
        else if(castOperation.getType().equals(Double.class))
        {
            if(!primitive.isNumber())
                throw new IllegalArgumentException("Value for double attribute modifier must be a number");
            castValue = castOperation.getType().cast(primitive.getAsDouble());
        }
        else if(castOperation.getType().equals(Boolean.class))
        {
            if(!primitive.isBoolean())
                throw new IllegalArgumentException("Value for boolean attribute modifier must be a boolean");
            castValue = castOperation.getType().cast(primitive.getAsBoolean());
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
}
