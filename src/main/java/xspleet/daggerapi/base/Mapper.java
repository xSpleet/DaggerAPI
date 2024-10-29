package xspleet.daggerapi.base;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.providers.ActionProvider;
import xspleet.daggerapi.providers.ConditionProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Mapper
{
    private static final Map<String, EntityAttribute> entityAttributes = new HashMap<>();
    private static final Map<String, EntityAttributeModifier.Operation> operations = new HashMap<>();
    private static final Map<String, ConditionProvider> conditionProviders = new HashMap<>();
    private static final Map<String, ActionProvider> actionProviders = new HashMap<>();
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

    public static Trigger getTrigger(String name)
    {
        if(!entityAttributes.containsKey(name))
            DaggerAPI.LOGGER.error("Trigger with the name " + name + " not found!");

        return triggers.get(name);
    }

    public static EntityAttributeModifier.Operation registerOperation(String name, EntityAttributeModifier.Operation operation)
    {
        operations.put(name, operation);
        return operation;
    }

    public static EntityAttributeModifier.Operation getOperation(String name)
    {
        if(!entityAttributes.containsKey(name))
            DaggerAPI.LOGGER.error("Operation with the name " + name + " not found!");

        return operations.get(name);
    }

    public static EntityAttribute registerEntityAttribute(String name, EntityAttribute attribute)
    {
        entityAttributes.put(name, attribute);
        return attribute;
    }

    public static EntityAttribute getEntityAttribute(String name)
    {
        if(!entityAttributes.containsKey(name))
            DaggerAPI.LOGGER.error("Attribute with the name " + name + " not found!");

        return entityAttributes.get(name);
    }

    public static ConditionProvider registerConditionProvider(String name, Function<Map<String, String>, Predicate<DaggerData>> provider)
    {
        ConditionProvider conditionProvider = conditionProviders.get(name);
        if(conditionProvider != null)
            return conditionProvider;

        conditionProvider = new ConditionProvider(name, provider);
        conditionProviders.put(name, conditionProvider);
        return conditionProvider;
    }

    public static ConditionProvider getConditionProvider(String name)
    {
        if(!conditionProviders.containsKey(name))
            DaggerAPI.LOGGER.error("Condition provider with the name " + name + " not found!");

        return conditionProviders.get(name);
    }

    public static ActionProvider registerActionProvider(String name, Function<Map<String, String>, Consumer<DaggerData>> provider)
    {
        ActionProvider actionProvider = actionProviders.get(name);
        if(actionProvider != null)
            return actionProvider;

        actionProvider = new ActionProvider(name, provider);
        actionProviders.put(name, actionProvider);
        return actionProvider;
    }

    public static ActionProvider getActionProvider(String name)
    {
        if(!actionProviders.containsKey(name))
            DaggerAPI.LOGGER.error("Action provider with the name " + name + " not found!");

        return actionProviders.get(name);
    }
}
