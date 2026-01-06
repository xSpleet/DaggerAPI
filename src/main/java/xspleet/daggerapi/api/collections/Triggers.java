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

    public static final Trigger TICK = registerTrigger("onTick")
            .setHasTriggerSource()
            .setWorldful();

    public static final Trigger BEFORE_DEATH = registerTrigger("beforeDeath")
            .setHasTriggerSource()
            .setWorldful()
            .addProvidedData(DaggerKeys.DAMAGE_AMOUNT)
            .addProvidedData(DaggerKeys.DAMAGE_SOURCE)
            .addProvidedData(DaggerKeys.ALLOW_DEATH);

    public static final Trigger KILL = registerTrigger("onKill")
            .setHasTriggerSource()
            .setWorldful()
            .addProvidedData(DaggerKeys.DAMAGE_SOURCE)
            .addProvidedData(DaggerKeys.VICTIM);

    public static final Trigger DEATH = registerTrigger("onDeath")
            .setHasTriggerSource()
            .setWorldful()
            .addProvidedData(DaggerKeys.DAMAGE_SOURCE);

    public static final Trigger ATTACK = registerTrigger("onAttack")
            .setHasTriggerSource()
            .setWorldful()
            .addProvidedData(DaggerKeys.DAMAGE_SOURCE)
            .addProvidedData(DaggerKeys.DAMAGE_AMOUNT)
            .addProvidedData(DaggerKeys.VICTIM);

    public static final Trigger JUMP = registerTrigger("onJump")
            .setHasTriggerSource()
            .setWorldful();

    public static final Trigger ON_CHANGE_DIMENSION = registerTrigger("onChangeDimension")
            .setHasTriggerSource()
            .setWorldful()
            .addProvidedData(DaggerKeys.ORIGIN)
            .addProvidedData(DaggerKeys.DESTINATION);
}
