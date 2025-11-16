package xspleet.daggerapi.api.collections;

import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.api.logging.LoggingContext;
import xspleet.daggerapi.trigger.Trigger;

import static xspleet.daggerapi.api.registration.Mapper.*;

public class Triggers
{
    public static void registerTriggers()
    {
        DaggerLogger.info(LoggingContext.STARTUP, "Registering triggers...");
    }

    public static final Trigger BEFORE_DAMAGE = registerTrigger("beforeDamage")
            .setHasTriggerSource()
            .setWorldful()
            .addProvidedData(DaggerKeys.DAMAGE_AMOUNT)
            .addProvidedData(DaggerKeys.DAMAGE_SOURCE);

    public static final Trigger ACTIVATE = registerTrigger("onActivate")
            .setHasTriggerSource()
            .setWorldful()
            .addProvidedData(DaggerKeys.ARTIFACT)
            .addProvidedData(DaggerKeys.SUCCESSFUL);

    public static final Trigger EAT = registerTrigger("onEat")
            .setHasTriggerSource()
            .setWorldful()
            .addProvidedData(DaggerKeys.FOOD_AMOUNT)
            .addProvidedData(DaggerKeys.SATURATION_AMOUNT)
            .addProvidedData(DaggerKeys.ITEM_STACK)
            .addProvidedData(DaggerKeys.ITEM);

    public static final Trigger TICK = registerTrigger("tick")
            .setHasTriggerSource()
            .setWorldful();
}
