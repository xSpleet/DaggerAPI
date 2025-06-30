package xspleet.daggerapi.collections.registration;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.collections.*;
import xspleet.daggerapi.base.Condition;
import xspleet.daggerapi.data.ProviderData;
import xspleet.daggerapi.trigger.Trigger;
import xspleet.daggerapi.trigger.actions.Action;
import xspleet.daggerapi.exceptions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Mapper
{
    public static void registerMapper()
    {
        DaggerAPI.LOGGER.info("Registering Mapper: ");
        Operations.registerOperations();
        ConditionProviders.registerConditionProviders();
        ActionProviders.registerActionProviders();
        Triggers.registerTriggers();
        EntityAttributes.registerEntityAttributes();
    }

    private static final Map<String, EntityAttribute> entityAttributes = new HashMap<>();
    private static final Map<String, EntityAttributeModifier.Operation> operations = new HashMap<>();
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

    public static Trigger getTrigger(String name) {
        DaggerAPI.LOGGER.info(triggers.values().stream().map(Trigger::getName).collect(Collectors.joining()));
        if(!triggers.containsKey(name))
        {
            DaggerAPI.LOGGER.error("Trigger with the name '{}' not found!", name);
            throw new NoSuchTriggerException(name);
        }

        return triggers.get(name);
    }

    public static EntityAttributeModifier.Operation registerOperation(String name, EntityAttributeModifier.Operation operation)
    {
        operations.put(name, operation);
        return operation;
    }

    public static EntityAttributeModifier.Operation getOperation(String name) {
        if(!operations.containsKey(name)) {
            DaggerAPI.LOGGER.error("Operation with the name '{}' not found!", name);
            throw new NoSuchOperationException(name);
        }
        return operations.get(name);
    }

    public static EntityAttribute registerEntityAttribute(String name, EntityAttribute attribute)
    {
        entityAttributes.put(name, attribute);
        return attribute;
    }

    public static EntityAttribute getEntityAttribute(String name) {
        if(!entityAttributes.containsKey(name)) {
            DaggerAPI.LOGGER.error("Attribute with the name '{}' not found!", name);
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

    public static Provider<Condition> getConditionProvider(String name) {
        if(!conditionProviders.containsKey(name)) {
            DaggerAPI.LOGGER.error("Condition provider with the name '{}' not found!", name);
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

    public static Provider<Action> getActionProvider(String name) {
        if(!actionProviders.containsKey(name)) {
            DaggerAPI.LOGGER.error("Action provider with the name '{}' not found!", name);
            throw new NoSuchActionException(name);
        }
        return actionProviders.get(name);
    }
}
