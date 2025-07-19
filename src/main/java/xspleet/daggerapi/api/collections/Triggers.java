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
            .setHasTriggerer()
            .setWorldful()
            .addProvidedData(DaggerKeys.AMOUNT)
            .addProvidedData(DaggerKeys.DAMAGE_SOURCE);

    public static final Trigger ACTIVATE = registerTrigger("activate")
            .setHasTriggerer()
            .setWorldful()
            .addProvidedData(DaggerKeys.ARTIFACT)
            .addProvidedData(DaggerKeys.SUCCESSFUL);
}
