package xspleet.daggerapi.collections.registration;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.mixin.MixinAttribute;
import xspleet.daggerapi.attributes.operations.AttributeOperation;
import xspleet.daggerapi.base.DaggerLogger;
import xspleet.daggerapi.collections.*;
import xspleet.daggerapi.base.Condition;
import xspleet.daggerapi.data.ProviderData;
import xspleet.daggerapi.trigger.Trigger;
import xspleet.daggerapi.trigger.actions.Action;
import xspleet.daggerapi.exceptions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Mapper
{
    public static void registerMapper()
    {
        DaggerLogger.info("Registering Mapper: ");
        Operations.registerOperations();
        ConditionProviders.registerConditionProviders();
        ActionProviders.registerActionProviders();
        Triggers.registerTriggers();
        Attributes.registerAttributes();
    }

    private static final Map<String, Attribute<?>> entityAttributes = new HashMap<>();
    private static final Map<String, AttributeOperation<?>> operations = new HashMap<>();
    private static final Map<String, Provider<Condition>> conditionProviders = new HashMap<>();
    private static final Map<String, Provider<Action>> actionProviders = new HashMap<>();
    private static final Map<String, Trigger> triggers = new HashMap<>();

    public static Trigger registerTrigger(String name)
    {
        Trigger trigger = triggers.get(name);
        if(trigger != null)
            return trigger;

        trigger = new Trigger(name);
        triggers.put(name, trigger);
        return trigger;
    }

    public static Trigger getTrigger(String name) throws NoSuchTriggerException {
        if(!triggers.containsKey(name))
        {
            DaggerLogger.error("Trigger with the name '{}' not found!", name);
            throw new NoSuchTriggerException(name);
        }
        return triggers.get(name);
    }

    public static <T> AttributeOperation<T> registerOperation(String name, AttributeOperation<T> operation)
    {
        operations.put(name, operation);
        return operation;
    }

    public static AttributeOperation<Double> registerOperation(String name, EntityAttributeModifier.Operation operation) {
        if(((Object)operation) instanceof AttributeOperation vanillaAttribute)
        {
            operations.put(name, vanillaAttribute);
            return (AttributeOperation<Double>) vanillaAttribute;
        }
        throw new IllegalStateException("Cannot register Vanilla entity attribute operation: " + operation.name());
    }

    public static AttributeOperation<?> getOperation(String name) throws NoSuchOperationException {
        if(!operations.containsKey(name)) {
            DaggerLogger.error("Operation with the name '{}' not found!", name);
            throw new NoSuchOperationException(name);
        }
        return operations.get(name);
    }

    public static <T> Attribute<T> registerAttribute(String name, Attribute<T> attribute)
    {
        entityAttributes.put(name, attribute);
        return attribute;
    }

    public static Attribute<Double> registerAttribute(String name, EntityAttribute attribute) {
        if(attribute instanceof MixinAttribute<?> vanillaAttribute)
        {
            entityAttributes.put(name, vanillaAttribute);
            return (Attribute<Double>) vanillaAttribute;
        }
        throw new IllegalStateException("Cannot register Vanilla entity attribute: " + attribute.getClass().getName());
    }

    public static Attribute<?> getAttribute(String name) throws NoSuchAttributeException {
        if(!entityAttributes.containsKey(name)) {
            DaggerLogger.error("Attribute with the name '{}' not found!", name);
            throw new NoSuchAttributeException(name);
        }
        return entityAttributes.get(name);
    }

    public static Provider<Condition> registerConditionProvider(String name, Function<ProviderData, Condition> provider)
    {
        Provider<Condition> conditionProvider = conditionProviders.get(name);
        if(conditionProvider != null)
            return conditionProvider;

        conditionProvider = new Provider<>(name, provider);
        conditionProviders.put(name, conditionProvider);
        return conditionProvider;
    }

    public static Provider<Condition> getConditionProvider(String name) throws NoSuchConditionException {
        if(!conditionProviders.containsKey(name)) {
            DaggerLogger.error("Condition provider with the name '{}' not found!", name);
            throw new NoSuchConditionException(name);
        }
        return conditionProviders.get(name);
    }

    public static Provider<Action> registerActionProvider(String name, Function<ProviderData, Action> provider)
    {
        Provider<Action> actionProvider = actionProviders.get(name);
        if(actionProvider != null)
            return actionProvider;

        actionProvider = new Provider<>(name, provider);
        actionProviders.put(name, actionProvider);
        return actionProvider;
    }

    public static Provider<Action> getActionProvider(String name) throws NoSuchActionException {
        if(!actionProviders.containsKey(name)) {
            DaggerLogger.error("Action provider with the name '{}' not found!", name);
            throw new NoSuchActionException(name);
        }
        return actionProviders.get(name);
    }

    public static String getNameOf(Attribute<?> attribute) {
        if(entityAttributes.containsValue(attribute)) {
            return entityAttributes.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(attribute))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No name found for attribute: " + attribute));
        }
        throw new IllegalArgumentException("Attribute is not registered: " + attribute);
    }

    public static String getNameOf(AttributeOperation<?> operation) {
        if(operations.containsValue(operation)) {
            return operations.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(operation))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No name found for operation: " + operation));
        }
        throw new IllegalArgumentException("Operation is not registered: " + operation);
    }
}
